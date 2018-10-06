package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.io.IOException
import java.sql.Connection
import java.sql.SQLException
import java.util.*

abstract class DatabaseConnector(protected val configuration: DatabaseConfiguration) {

    protected abstract val connection: Connection

    fun addUser(name: String, password: String, email: String) {
        //val sql = "INSERT INTO users(name, password, email) VALUES (?, ?, ?)"

        val sql = "INSERT INTO users(name, password, email) VALUES (?, ?, ?)"

        try {
            val statement = connection.prepareStatement(sql)

            statement.setString(1, name)
            statement.setString(2, BCrypt.hashpw(password, BCrypt.gensalt(4)))
            statement.setString(3, email)

            statement.executeUpdate()
        } catch (e: SQLException) { // TODO error handling?
            System.out.println(e.message)
            e.printStackTrace()
        }
    }

    fun getAllUsers(): JSONArray {
        val users = JSONArray()

        val sql = "SELECT * FROM users"

        try {
            val statement = connection.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val user = JSONObject()
                // TODO there has to be a better way to do this
                user.put("id", resultSet.getInt("id"))
                user.put("name", resultSet.getString("name"))
                user.put("password", resultSet.getString("password"))
                user.put("email", resultSet.getString("email"))
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
        val result = JSONObject()

        val sql = "SELECT id, password FROM users WHERE email = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, username)
            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                if(BCrypt.checkpw(password, resultSet.getString("password"))) {
                    result.put("token", addNewTokenForUse(resultSet.getInt("id")))
                    return result
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
        val sql = "UPDATE users SET token = ? WHERE token = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, null)
            statement.setString(2, token)
            if(statement.executeUpdate() == 0) {
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
        // TODO check if admin or not, returning always admin atm
        val sql = "SELECT * FROM users WHERE token = ?"
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, token)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                // TODO return if admin or not, also if more than one result, then something wrong
                return true
            }
            // No results found, so token is wrong
            throw AuthenticationException()
        } catch (e: SQLException) {
            println(e.message)
            throw SQLException("Database error")
        }
    }


    //TODO maybe get a better exeption here
    @Throws(SQLException::class)
    private fun addNewTokenForUse(id: Int): String {
        val token = UUID.randomUUID()

        val sql = "UPDATE users SET token = ? WHERE id = ?"

        val statement = connection.prepareStatement(sql)
        statement.setString(1, token.toString())
        statement.setInt(2, id)

        // Update failed on 0
        if(statement.executeUpdate() == 0) {
            throw SQLException("Failed to update table, check log")
        }

        return token.toString()
    }

    protected fun createInitialTables() {
        val file = ResourceUtils.getFile("classpath:database-layout.sql")
        try {
            val input = Scanner(file)

            while (input.hasNextLine()) {
                var line = input.nextLine()

                // Change SQL syntax to fit PGSQL if needed
                if(configuration.type.equals("pgsql"))
                    line = line.replace("INTEGER PRIMARY KEY", "SERIAL PRIMARY KEY")

                if (line.startsWith("CREATE TABLE")) {
                    try {
                        val statement = connection.createStatement()
                        statement.execute(line)
                    } catch (e: SQLException) {
                        println(e.message)
                        e.printStackTrace()
                    }
                }
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }
}