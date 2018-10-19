package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Repository
import java.lang.Exception

// TODO move configuration to spring style bean thingy. Or maybe not, easier to mock this way
@Repository
class DatabaseHandler(private val configuration: DatabaseConfiguration) {

    // TODO move get this through Autowire or some other mnannter, shouldn't probably creat einstance
    private val databaseConnector = run { when (configuration.type) {
        "sqlite" -> SQLiteConnector(configuration)
        "pgsql" -> PgSQLConnector(configuration)
        else -> throw RuntimeException("Unsupported database configured.")
    } }

    @Throws(Exception::class)
    fun getAllUsers() = databaseConnector.getAllUsers()
    @Throws(Exception::class)
    fun addUser(firstName: String, lastName: String, pass: String, email: String, admin: Boolean) =
            databaseConnector.addUser(firstName, lastName, pass, email, admin)
    @Throws(Exception::class)
    fun addArticle(name: String, brand: String?, quantity: Int, shelf: String): Int =
            databaseConnector.addArticle(name, brand, quantity, shelf)
    @Throws(Exception::class)
    fun getAllArticles() = databaseConnector.getAllArticles()
    @Throws(Exception::class)
    fun logout(token: String) = databaseConnector.logout(token)
    @Throws(Exception::class)
    fun login(username: String, password: String) = databaseConnector.login(username, password)
    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean = databaseConnector.authenticateToken(token)
}