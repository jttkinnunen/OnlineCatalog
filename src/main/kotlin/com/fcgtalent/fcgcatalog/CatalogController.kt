package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.UploadConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseConnector
import com.fcgtalent.fcgcatalog.util.AddArticleBody
import com.fcgtalent.fcgcatalog.util.AddLocationBody
import com.fcgtalent.fcgcatalog.util.AddUserBody
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.FileTypeException
import com.fcgtalent.fcgcatalog.util.GetArticlesBody
import com.fcgtalent.fcgcatalog.util.GetArticlesInLocationsBody
import com.fcgtalent.fcgcatalog.util.GetLocationsBody
import com.fcgtalent.fcgcatalog.util.GetSelfBody
import com.fcgtalent.fcgcatalog.util.GetUsersBody
import com.fcgtalent.fcgcatalog.util.LoginBody
import com.fcgtalent.fcgcatalog.util.LogoutBody
import com.fcgtalent.fcgcatalog.util.SetArticlesAtLocationBody
import com.fcgtalent.fcgcatalog.util.SetPasswordBody
import com.fcgtalent.fcgcatalog.util.StartPasswordResetBody
import com.fcgtalent.fcgcatalog.util.UpdateArticleBody
import com.fcgtalent.fcgcatalog.util.UpdateLocationBody
import com.fcgtalent.fcgcatalog.util.UpdateUserBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.sql.SQLException

@RestController
class CatalogController {

    @Autowired
    private lateinit var databaseConnector: DatabaseConnector

    @Autowired
    private lateinit var uploadConfiguration: UploadConfiguration

    data class OurError(val error: String)

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
        publicCall: Boolean,
        call: (Unit) -> Any?
    ): ResponseEntity<Any> {
        return try {
            // Checks that user has token and if command is adminOnly, check that use is admin
            if (!publicCall) {
                val admin = databaseConnector.authenticateToken(token ?: "")
                if (adminOnly && !admin) {
                    throw AuthenticationException("This requires admin permissions.")
                }
            }
            // TODO actually check if it was successfull
            ResponseEntity(call(Unit).let { it ?: "{ success: true }" }, HttpStatus.OK)
        } catch (e: SQLException) {
            ResponseEntity(OurError("Database Error"), HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: AuthenticationException) {
            e.toResponseEntity() as ResponseEntity<Any>
        } catch (e: Exception) {
            // Generic exception
            ResponseEntity(OurError(e.message ?: "Unknown error"), HttpStatus.OK)
        }
    }

    @CrossOrigin
    @PostMapping("/addUser", MediaType.APPLICATION_JSON_VALUE)
    fun addUser(
        @RequestBody addUserBody: AddUserBody
    ): ResponseEntity<Any> {
        return encapsulateCall(addUserBody.token, true, false) {
            databaseConnector.addUser(
                addUserBody.firstName,
                addUserBody.lastName,
                addUserBody.email,
                addUserBody.admin
            )
        }
    }

    // TODO Allow updating of self
    @CrossOrigin
    @PostMapping("/updateUser", MediaType.APPLICATION_JSON_VALUE)
    fun updateUser(
        @RequestBody updateUserBody: UpdateUserBody
    ): ResponseEntity<Any> {
        return encapsulateCall(updateUserBody.token, true, false) {
            databaseConnector.updateUser(
                updateUserBody.id,
                updateUserBody.firstName,
                updateUserBody.lastName,
                updateUserBody.email,
                updateUserBody.admin
            )
            null
        }
    }

    @CrossOrigin
    @PostMapping("/getUsers", MediaType.APPLICATION_JSON_VALUE)
    fun getUsers(@RequestBody getUsersBody: GetUsersBody): ResponseEntity<Any> {
        return encapsulateCall(getUsersBody.token, true, false) {
            databaseConnector.getUsers(getUsersBody.ids ?: listOf())
        }
    }

    @CrossOrigin
    @PostMapping("/getSelf", MediaType.APPLICATION_JSON_VALUE)
    fun getSelf(@RequestBody getSelfBody: GetSelfBody): ResponseEntity<Any> {
        return encapsulateCall(getSelfBody.token, false, false) {
            databaseConnector.getUserWithToken(getSelfBody.token)
        }
    }

    @CrossOrigin
    @PostMapping("/addArticle", MediaType.APPLICATION_JSON_VALUE)
    fun addArticle(
        @RequestBody addArticle: AddArticleBody
    ): ResponseEntity<Any> {
        return encapsulateCall(addArticle.token, false, false) {
            databaseConnector.addArticle(
                addArticle.name,
                addArticle.description
            )
        }
    }

    @CrossOrigin
    @PostMapping("/updateArticle", MediaType.APPLICATION_JSON_VALUE)
    fun updateArticle(
        @RequestBody updateArticle: UpdateArticleBody
    ): ResponseEntity<Any> {
        return encapsulateCall(updateArticle.token, false, false) {
            databaseConnector.updateArticle(
                updateArticle.id,
                updateArticle.name,
                updateArticle.description
            )
            null
        }
    }

    @CrossOrigin
    @PostMapping("/uploadImage", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun uploadImage(
        @RequestParam("token") token: String,
        @RequestParam("id") id: Int,
        @RequestParam("image") image: MultipartFile
    ): ResponseEntity<Any> {
        return encapsulateCall(token, false, false) {
            val article = databaseConnector.getArticles(articleIds = listOf(id), locationIds = listOf())
            if(article.size == 1) {
                uploadImage(image, id)
            } else {
                throw Exception("Problems finding Article.")
            }
            null
        }
    }

    @CrossOrigin
    @PostMapping("/getArticlesDetailed", MediaType.APPLICATION_JSON_VALUE)
    fun getArticlesDetailed(@RequestBody getArticlesBody: GetArticlesBody): ResponseEntity<Any> {
        return encapsulateCall(getArticlesBody.token, false, false) {
            databaseConnector.getArticles(getArticlesBody.ids ?: listOf(), listOf(), true)
        }
    }

    @CrossOrigin
    @PostMapping("/getArticles", MediaType.APPLICATION_JSON_VALUE)
    fun getArticles(@RequestBody getArticlesInLocationsBody: GetArticlesInLocationsBody): ResponseEntity<Any> {
        return encapsulateCall(getArticlesInLocationsBody.token, false, false) {
            databaseConnector.getArticles(
                getArticlesInLocationsBody.articleIds ?: listOf(),
                getArticlesInLocationsBody.locationIds ?: listOf()
            )
        }
    }

    @CrossOrigin
    @PostMapping("/setArticlesAtLocation", MediaType.APPLICATION_JSON_VALUE)
    fun setArticlesAtLocation(@RequestBody setArticlesAtLocationBody: SetArticlesAtLocationBody): ResponseEntity<Any> {
        return encapsulateCall(setArticlesAtLocationBody.token, false, false) {
            databaseConnector.setArticlesAtLocation(
                setArticlesAtLocationBody.locationId,
                setArticlesAtLocationBody.articleId,
                setArticlesAtLocationBody.quantity
            )
            null
        }
    }

    @CrossOrigin
    @PostMapping("/addLocation", MediaType.APPLICATION_JSON_VALUE)
    fun addLocation(
        @RequestBody addLocationBody: AddLocationBody
    ): ResponseEntity<Any> {
        return encapsulateCall(addLocationBody.token, true, false) {
            databaseConnector.addLocation(
                addLocationBody.name
            )
        }
    }

    @CrossOrigin
    @PostMapping("/updateLocation", MediaType.APPLICATION_JSON_VALUE)
    fun updateLocation(
        @RequestBody updateLocation: UpdateLocationBody
    ): ResponseEntity<Any> {
        return encapsulateCall(updateLocation.token, true, false) {
            databaseConnector.updateLocation(
                updateLocation.id,
                updateLocation.name
            )
        }
    }

    @CrossOrigin
    @PostMapping("/getLocations", MediaType.APPLICATION_JSON_VALUE)
    fun getLocations(@RequestBody getLocationsBody: GetLocationsBody): ResponseEntity<Any> {
        return encapsulateCall(getLocationsBody.token, true, false) {
            databaseConnector.getLocations(getLocationsBody.ids ?: listOf())
        }
    }

    @CrossOrigin
    @PostMapping("/logout", MediaType.APPLICATION_JSON_VALUE)
    fun logout(@RequestBody logoutBody: LogoutBody): ResponseEntity<Any> {
        return encapsulateCall(logoutBody.token, false, false) { databaseConnector.logout(logoutBody.token) }
    }

    @CrossOrigin
    @PostMapping("/login", MediaType.APPLICATION_JSON_VALUE)
    fun login(
        @RequestBody loginBody: LoginBody
    ): ResponseEntity<Any> {
        return encapsulateCall(null, false, true) { databaseConnector.login(loginBody.username, loginBody.password) }
    }

    @CrossOrigin
    @PostMapping("/setPassword")
    fun setPassword(@RequestBody setPasswordBody: SetPasswordBody): ResponseEntity<Any> {
        return encapsulateCall(null, adminOnly = false, publicCall = true) {
            databaseConnector.setPassword(
                newPassword = setPasswordBody.newPassword,
                resetToken = setPasswordBody.resetToken
            )
        }
    }

    @CrossOrigin
    @PostMapping("/startPasswordReset")
    fun startPasswordReset(@RequestBody startPasswordResetBody: StartPasswordResetBody): ResponseEntity<Any> {
        return encapsulateCall(null, adminOnly = false, publicCall = true) {
            databaseConnector.initiatePasswordReset(startPasswordResetBody.email)
        }
    }

    @Throws(Exception::class)
    private fun uploadImage(image: MultipartFile, id: Int) {
        val fileEnding: String
        when (image.contentType) {
            "image/jpeg" -> fileEnding = "jpg"
            "image/png" -> fileEnding = ".png"
            else -> {
                throw FileTypeException()
            }
        }
        // TODO take care of slash missing in config
        val path = Paths.get("${uploadConfiguration.path}$id$fileEnding")
        println(path.toAbsolutePath())
        Files.copy(image.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
        databaseConnector.setArticleImage(id, path.toString())
    }
}
