package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.DatabaseConfiguration
import java.sql.DriverManager

class SQLiteConnector(configuration: DatabaseConfiguration) : DatabaseConnector(configuration) {

    override val connection = if (configuration.sqlitememory) DriverManager.getConnection("jdbc:sqlite::memory:")
        else DriverManager.getConnection("jdbc:sqlite:${configuration.name}.db")

    init {
        createInitialTables()

        // TODO Remove this test info
        addUser("test1", "testing", "test@test.com")
        addUser("test3", "testing3", "test@test3.com")
    }
}