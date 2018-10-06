package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.UploadConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import com.fcgtalent.fcgcatalog.util.AuthenticationException
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

    @PostMapping("/addUser")
    fun addUser(
        @RequestParam("name") name: String,
        @RequestParam("password") password: String,
        @RequestParam("email") email: String,
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        val admin: Boolean
        try {
            if (!databaseHandler.authenticateToken(token)) {
                throw AuthenticationException("This requires admin permissions.")
            }
        } catch (e: SQLException) { //TODO these try catch are repeating like crazy, think of a solution
            val error = JSONObject()
            error.put("error", "Database Error")
            return ResponseEntity(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: AuthenticationException) {
            return e.toResponseEntity()
        }

        databaseHandler.addUser(name, password, email)
        return ResponseEntity<String>("", HttpStatus.OK)
    }

    // TODO Authentication?
    @GetMapping("/showUsers")
    fun users() = databaseHandler.getAllUsers().toString()

    // TODO demand authentication to upload and check it. Also make sure file is correctly linked to what it belongs to
    // TODO maybe don't need to redirect, but return beter stuff? Something helpful
    @PostMapping("/upload")
    fun imageUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        if (file.isEmpty) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload")
            return "redirect:uploadStatus"
        }

        when (file.contentType) {
            "image/jpeg", "image/png" -> println() // TODO maybe do name here, so you know to add correct ending
            else -> {
                println("Attempted to upload ${file.contentType}")
                redirectAttributes.addFlashAttribute("message", "Only upload jpeg or png files.")
                return "redirect:uploadStatus"
            }
        }

        try {
            // TODO set correct name, based on the id of the item
            val path = Paths.get("${uploadConfiguration.path}${file.originalFilename}")

            println(path.toAbsolutePath())

            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded ${file.originalFilename}")
        } catch (e: IOException) {
            redirectAttributes.addFlashAttribute("message", "Error uploading file.")
            e.printStackTrace()
        }
        return "redirect:/uploadStatus"
    }

    // TODO do this
    @GetMapping("uploadStatus")
    fun uploadStatus(): String {
        return "upload status"
    }

    @PostMapping("/logout")
    fun logout(@RequestParam("token") token: String): ResponseEntity<String> {
        try {
            databaseHandler.logout(token)
            return ResponseEntity("", HttpStatus.OK)
        } catch (e: SQLException) {
            val error = JSONObject()
            error.put("error", "Database Error")
            return ResponseEntity(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: AuthenticationException) {
            return e.toResponseEntity()
        }
    }

    //TODO Move normal exception to something else
    // TODO ADD response entity stuff, Also accept email in the future, checck authetnicaiton also
    @PostMapping("/login")
    fun login(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<String> {
        System.out.println("Got user ${username} and pass $password")

        try {
            val token = databaseHandler.login(username, password)
            println("Returning accepted")
            return ResponseEntity(token.toString(), HttpStatus.OK)
        } catch (e: SQLException) {
            val error = JSONObject()
            error.put("error", "Database Error")
            return ResponseEntity(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: AuthenticationException) {
            return e.toResponseEntity()
        }
    }
}

