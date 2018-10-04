package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import java.sql.Connection

class PgSQLConnector(configuration: DatabaseConfiguration) : DatabaseConnector(configuration) {

    // TODO this needs to done with task #21

    override val connection: Connection
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
}