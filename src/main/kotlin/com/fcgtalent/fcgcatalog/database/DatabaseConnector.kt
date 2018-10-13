package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import org.postgresql.util.PSQLException
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.io.IOException
import java.sql.Connection
import java.sql.SQLException
import java.util.UUID

// TODO maybe clean this kinda like how the REST interface was cleaned up a bit
abstract class DatabaseConnector(protected val configuration: DatabaseConfiguration) {

    protected abstract val connection: Connection

    companion object {
        const val TABLE_USERS = "users"

        const val FIELD_ID = "id"
        const val FIELD_FIRST_NAME = "first_name"
        const val FIELD_LAST_NAME = "last_name"
        const val FIELD_PASSWORD = "password"
        const val FIELD_EMAIL = "email"
        const val FIELD_ADMIN = "admin"
        const val FIELD_TOKEN = "token"
    }


    fun addUser(firstName: String, lastName: String, password: String, email: String, admin: Boolean) {
        val sql = "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_PASSWORD, $FIELD_EMAIL, $FIELD_ADMIN) VALUES (?, ?, ?, ?, ?)"

        try {
            val statement = connection.prepareStatement(sql)

            statement.setString(1, firstName)
            statement.setString(2, lastName)
            statement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt(4)))
            statement.setString(4, email)
            statement.setInt(5, if(admin) 1 else 0)

            statement.executeUpdate()
        } catch (e: SQLException) { // TODO error handling?
            System.out.println(e.message)
            e.printStackTrace()
        }
    }

    fun getAllUsers(): JSONArray {
        val users = JSONArray()

        val sql = "SELECT * FROM $TABLE_USERS"

        try {
            val statement = connection.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val user = JSONObject()
                user.put(FIELD_ID, resultSet.getInt(FIELD_ID))
                user.put(FIELD_FIRST_NAME, resultSet.getString(FIELD_FIRST_NAME))
                user.put(FIELD_LAST_NAME, resultSet.getString(FIELD_LAST_NAME))
                user.put(FIELD_EMAIL, resultSet.getString(FIELD_EMAIL))
                user.put(FIELD_ADMIN, resultSet.getInt(FIELD_ADMIN) == 1)

                users.put(user)
            }
        } catch (e: SQLException) { // TODO error handling?
            println(e.message)
            e.printStackTrace()
        }
        return users
    }

    @Throws(Exception::class)
    fun login(username: String, password: String): JSONObject {
        val sql = "SELECT $FIELD_ID, $FIELD_PASSWORD FROM $TABLE_USERS WHERE $FIELD_EMAIL = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, username)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                if (BCrypt.checkpw(password, resultSet.getString("password"))) {
                    return addNewTokenForUse(resultSet.getInt(FIELD_ID))
                }
            }
            throw AuthenticationException()
        } catch (e: SQLException) {
            println(e.message)
            throw SQLException("Database error")
        }
    }

    @Throws(Exception::class)
    fun logout(token: String) {
        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_TOKEN = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, null)
            statement.setString(2, token)
            if (statement.executeUpdate() == 0) {
                // Nothing was updated, so token must not have been valid
                throw AuthenticationException()
            }
        } catch (e: SQLException) {
            println(e.message)
            throw SQLException("Database error")
        }
    }

    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean {
        val sql = "SELECT $FIELD_ADMIN FROM $TABLE_USERS WHERE $FIELD_TOKEN = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, token)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                return resultSet.getInt(FIELD_ADMIN) == 1
            }
            // No results found, so token is wrong
            throw AuthenticationException()
        } catch (e: SQLException) {
            println(e.message)
            throw SQLException("Database error")
        }
    }

    // TODO maybe get a better exeption here
    @Throws(SQLException::class)
    private fun addNewTokenForUse(id: Int): JSONObject {
        val token = UUID.randomUUID()

        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_ID = ?"

        val statement = connection.prepareStatement(sql)
        statement.setString(1, token.toString())
        statement.setInt(2, id)

        // Update failed on 0
        if (statement.executeUpdate() == 0) {
            throw SQLException("Failed to update table, check log")
        }

        val json = JSONObject()
        json.put(FIELD_TOKEN, token.toString())
        return json
    }

    open protected fun createInitialTables() {
        val dropStatement = connection.createStatement()
        // TODO clean this up, should drop all tables, now just what we specify
        try {
            dropStatement.execute("DROP TABLE $TABLE_USERS")
            // Catching Excerption, since multi exception not supported on Kotlin yet, also should clean this code
        } catch (e: Exception) {
            // TODO just ignoring does not exist error, entire thingy should be cleaned anyway, fix then
        }
        val file = when(configuration.type) {
            "sqlite"-> ResourceUtils.getFile("classpath:database-layout-sqlite.sql")
            "pgsql" -> ResourceUtils.getFile("classpath:database-layout-pgsql.sql")
            // TODO probably change this to different exception, maybe customn
            else -> throw SQLException("Unknown database configuration")
        }

        val statement = connection.createStatement()
        statement.execute(file.readText(Charsets.UTF_8))
    }
}