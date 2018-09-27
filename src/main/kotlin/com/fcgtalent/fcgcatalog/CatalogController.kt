package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong



// For basic testing purposes now, TODO remove later
data class Greeting(val id: Long, val content: String)

@RestController
class CatalogController {

    @Autowired
    private lateinit var databaseHandler: DatabaseHandler

    // For basic testing purposes now, TODO remove later
    val counter = AtomicLong()
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")


//    @RequestMapping("/adduser", method = arrayOf(RequestMethod.POST))
//    fun addUser(
//        @RequestParam(value = "name") name: String,
//        @RequestParam(value = "password") password: String,
//        @RequestParam(value = "email") email: String) = {
//        System.out.println("Got add user $name pass $password mail $email")
//        databaseHandler.addUser(name, password, email)
//    }
    //TODO ADD response entity stuff
    //TODO Also accept email in the future
    @RequestMapping("/adduser", method = arrayOf(RequestMethod.POST))
    fun addUser(@RequestBody user: SecurityProperties.User) : ResponseEntity<String> {
        System.out.println("Got user ${user.name}")
        //TODO probably handle the user better
        databaseHandler.addUser(user.name, user.password, "Moo@moo.fi")
        return ResponseEntity<String>("Good", HttpStatus.CREATED)
    }


    @GetMapping("/showusers")
    fun users() = databaseHandler.getAllUsers().toString()
//        val databaseHandler =  getBean(DatabaseHandler::class.java, config)
}

