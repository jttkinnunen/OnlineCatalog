package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.DriverManager

class PgSQLConnector(configuration: DatabaseConfiguration) : DatabaseConnector(configuration) {

    // TODO this needs to done with task #21

    override val connection: Connection
        get() {
            var conn: Connection
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://${configuration.address}:${configuration.port}/${configuration.name}", configuration.username, configuration.password)

            } catch (e: PSQLException) {
                if(e.message!!.contains("database \"${configuration.name}\" does not exist")) {
                    conn = DriverManager.getConnection("jdbc:postgresql://${configuration.address}:${configuration.port}/", configuration.username, configuration.password)
                    val statement = conn.createStatement()
                    statement.execute("CREATE DATABASE ${configuration.name}")
                    return DriverManager.getConnection("jdbc:postgresql://${configuration.address}:${configuration.port}/${configuration.name}", configuration.username, configuration.password)
                } else {
                    println("Throwing forward")
                    throw e
                }
            }
            return conn

        }
    init {
        createInitialTables()

        // TODO Remove this test info
//        addUser("test1", "testlast", "testing", "test@test.com", true)
//        addUser("test3", "tes3last", "testing3", "test@test3.com", false)
    }
}