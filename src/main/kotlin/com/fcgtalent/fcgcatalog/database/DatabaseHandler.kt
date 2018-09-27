package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.DatabaseConfiguration
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.stereotype.Repository

//TODO move configuration to spring style bean thingy.
@Repository
class DatabaseHandler(private val configuration: DatabaseConfiguration) {

    //TODO move get this through Autowire or some other mnannter, shouldn't probably creat einstance
    private val databaseConnector = run {  when(configuration.type) {
        "sqlite" -> SQLiteConnector(configuration)
        "pgsql" -> PgSQLConnector(configuration)
        else -> throw RuntimeException("Unsupported database configured.")
    } }


    fun getAllUsers(): JSONArray = databaseConnector.getAllUsers()
    fun addUser(name: String, pass: String, email: String) = databaseConnector.addUser(name, pass, email)
}