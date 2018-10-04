package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.UploadConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.atomic.AtomicLong

// For basic testing purposes now, TODO remove later
data class Greeting(val id: Long, val content: String)

@RestController
class CatalogController {

    @Autowired
    private lateinit var databaseHandler: DatabaseHandler

    @Autowired
    private lateinit var uploadConfiguration: UploadConfiguration

    // For basic testing purposes now, TODO remove later
    val counter = AtomicLong()
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

    // TODO ADD response entity stuff, Also accept email in the future
    @RequestMapping("/addUser", method = arrayOf(RequestMethod.POST))
    fun addUser(@RequestBody user: SecurityProperties.User): ResponseEntity<String> {
        System.out.println("Got user ${user.name}")
        // TODO probably handle the user better
        databaseHandler.addUser(user.name, user.password, "Moo@moo.fi")
        return ResponseEntity<String>("Good", HttpStatus.CREATED)
    }

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
}
