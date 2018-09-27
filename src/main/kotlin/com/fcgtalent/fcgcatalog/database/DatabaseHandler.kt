package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.DatabaseConfiguration
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.stereotype.Repository

@Repository
class DatabaseHandler(private val configuration: DatabaseConfiguration) {

//
//    @Value("\${database.type}")
//    private lateinit var databaseType: String

    private val databaseConnector = run {  when(configuration.type) {
        "sqlite" -> SQLiteConnector(configuration)
        "pgsql" -> PgSQLConnector(configuration)
        else -> throw RuntimeException("Unsupported database configured.")
    } }


    fun getAllUsers(): JSONArray = databaseConnector.getAllUsers()
    fun addUser(name: String, pass: String, email: String) = databaseConnector.addUser(name, pass, email)
}