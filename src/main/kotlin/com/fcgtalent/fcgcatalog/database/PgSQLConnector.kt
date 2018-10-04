package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.DatabaseConfiguration
import java.sql.Connection
import java.sql.DriverManager

class PgSQLConnector(configuration: DatabaseConfiguration) : DatabaseConnector(configuration) {

    // TODO this needs to done with task #21

    override val connection: Connection
        get() = DriverManager.getConnection("jdbc:postgresql://${configuration.address}:${configuration.port}/${configuration.name}", configuration.username, configuration.password)

    init {
        createInitialTables()

        // TODO Remove this test info
        addUser("test1", "testing", "test@test.com")
        addUser("test3", "testing3", "test@test3.com")
    }
}