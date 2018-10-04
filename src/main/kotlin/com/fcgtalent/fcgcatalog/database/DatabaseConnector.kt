package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.DatabaseConfiguration
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.io.IOException
import java.sql.Connection
import java.sql.SQLException
import java.util.Scanner

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