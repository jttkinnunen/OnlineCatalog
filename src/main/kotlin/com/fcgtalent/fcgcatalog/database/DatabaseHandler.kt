package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Repository
import java.lang.Exception

// TODO move configuration to spring style bean thingy.
@Repository
class DatabaseHandler(private val configuration: DatabaseConfiguration) {

    // TODO move get this through Autowire or some other mnannter, shouldn't probably creat einstance
    private val databaseConnector = run { when (configuration.type) {
        "sqlite" -> SQLiteConnector(configuration)
        "pgsql" -> PgSQLConnector(configuration)
        else -> throw RuntimeException("Unsupported database configured.")
    } }

    fun getAllUsers(): JSONArray = databaseConnector.getAllUsers()
    fun addUser(name: String, pass: String, email: String) = databaseConnector.addUser(name, pass, email)
    @Throws(Exception::class)
    fun logout(token: String) = databaseConnector.logout(token)
    @Throws(Exception::class)
    fun login(username: String, password: String): JSONObject = databaseConnector.login(username, password)
    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean = databaseConnector.authenticateToken(token)
}