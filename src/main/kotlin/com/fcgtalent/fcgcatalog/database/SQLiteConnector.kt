package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import java.sql.DriverManager

class SQLiteConnector(configuration: DatabaseConfiguration) : DatabaseConnector(configuration) {

    override val connection = if (configuration.sqlitememory) DriverManager.getConnection("jdbc:sqlite::memory:")
        else DriverManager.getConnection("jdbc:sqlite:${configuration.name}.db")

    init {
        createInitialTables()

        // TODO Remove this test info
        addUser("test1", "testlast", "testing", "test@test.com", true)
        addUser("test3", "tes3last", "testing3", "test@test3.com", false)
        addUser("antti", "pantti", "hiano", "elefantti@hiano.fi", true)
    }
}