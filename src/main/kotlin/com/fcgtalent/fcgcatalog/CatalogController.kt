package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.UploadConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.FileTypeException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.sql.SQLException

@RestController
class CatalogController {

    @Autowired
    private lateinit var databaseHandler: DatabaseHandler

    @Autowired
    private lateinit var uploadConfiguration: UploadConfiguration

    /**
     * Helper function used to reduce the clutter, by moving any call inside the common try catch
     * Also includes checking for authentication, (admin)
     * @param token Users current token, used to authenticate the user. Maybe null if user is logging in
     * @param adminOnly This call requires admin permissions
     * @param call Call or lambda we wish to call inside our try catches.
     * @return Returns a ResponseEntity that is then returned as REST interface answer
     */
    private fun encapsulateCall(
        token: String?,
        adminOnly: Boolean,
        call: (Unit) -> Any
    ): ResponseEntity<String> {
        return try {
            // Checks that user has token and if command is adminOnly, check that use is admin
            if (token != null && (adminOnly && !databaseHandler.authenticateToken(token))) {
                throw AuthenticationException("This requires admin permissions.")
            }
            ResponseEntity(call(Unit).let { (it as? JSONObject)?.toString() ?: "" }, HttpStatus.OK)
        } catch (e: SQLException) {
            val error = JSONObject()
            error.put("error", "Database Error")
            ResponseEntity(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: AuthenticationException) {
            e.toResponseEntity()
        }
    }

    @PostMapping("/addUser")
    fun addUser(
        @RequestParam("name") firstName: String,
        @RequestParam("lastname") lastName: String,
        @RequestParam("password") password: String,
        @RequestParam("email") email: String,
        @RequestParam("admin") admin: Boolean,
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        return encapsulateCall(token, true) { databaseHandler.addUser(firstName, lastName, password, email, admin) }
    }

    @PostMapping("/getUsers")
    fun getUsers(@RequestParam("token") token: String): ResponseEntity<String> {
        return encapsulateCall(token, true) { databaseHandler.getAllUsers() }
    }

    @PostMapping("/addArticle")
    fun addArticle(
        @RequestParam("name") name: String,
        @RequestParam("brand") brand: String?,
        @RequestParam("quantity") quantity: Int = 0,
        @RequestParam("shelf") shelf: String,
        @RequestParam("image") image: MultipartFile? ,
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        return encapsulateCall(token, false) {
            val id = databaseHandler.addArticle(name, brand, quantity, shelf)

            image?.let { uploadImage(it, "article_$id") }

            Any()
        }
    }

    @PostMapping("/getArticles")
    fun getArticles(@RequestParam("token") token: String): ResponseEntity<String> {
        return encapsulateCall(token, false) { databaseHandler.getAllArticles() }
    }

    // TODO do this or remove this, part of imageUpload interface so do at the same time
    @GetMapping("uploadStatus")
    fun uploadStatus(): String {
        return "upload status"
    }

    @PostMapping("/logout")
    fun logout(@RequestParam("token") token: String): ResponseEntity<String> {
        return encapsulateCall(token, false) { databaseHandler.logout(token) }
    }

    // TODO Move normal exception to something else
    // TODO ADD response entity stuff, Also accept email in the future, checck authetnicaiton also
    @PostMapping("/login")
    fun login(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<String> {
        return encapsulateCall(null, false) { databaseHandler.login(username, password) }
    }


    @Throws(Exception::class)
    private fun uploadImage(image: MultipartFile, name: String) {
        val fileEnding: String
        when (image.contentType) {
            "image/jpeg" -> fileEnding = "jpg"
            "image/png" -> fileEnding = ".png"
            else -> {
                throw FileTypeException()
            }
        }
        // TODO set correct name, based on the id of the item
        val path = Paths.get("${uploadConfiguration.path}$name$fileEnding")
        println(path.toAbsolutePath())
        Files.copy(image.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
    }
}
