package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.ArticleResult
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.UserResult
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement
import java.util.UUID

// TODO maybe clean this kinda like how the REST interface was cleaned up a bit
abstract class DatabaseConnector(protected val configuration: DatabaseConfiguration) {

    protected abstract val connection: Connection

    companion object {
        const val TABLE_USERS = "users"
        const val TABLE_ARTICLES = "articles"

        // User fields
        const val FIELD_ID = "id"
        const val FIELD_FIRST_NAME = "first_name"
        const val FIELD_LAST_NAME = "last_name"
        const val FIELD_PASSWORD = "password"
        const val FIELD_EMAIL = "email"
        const val FIELD_ADMIN = "admin"
        const val FIELD_TOKEN = "token"

        // Article fields
        const val FIELD_NAME = "name"
        const val FIELD_BRAND = "brand"
        const val FIELD_QUANTITY = "quantity"
        const val FIELD_LAST_CHANGE = "last_change"
        const val FIELD_SHELF = "shelf"
    }

    @Throws(SQLException::class)
    fun addUser(firstName: String, lastName: String, password: String, email: String, admin: Boolean) {
        val sql =
            "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_PASSWORD, $FIELD_EMAIL, $FIELD_ADMIN) VALUES (?, ?, ?, ?, ?)"

        val statement = connection.prepareStatement(sql)

        statement.setString(1, firstName)
        statement.setString(2, lastName)
        statement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt(4)))
        statement.setString(4, email)
        statement.setInt(5, if (admin) 1 else 0)

        statement.executeUpdate()
    }

    @Throws(SQLException::class)
    fun getAllUsers(): List<UserResult> {
        val userList = ArrayList<UserResult>()

        val sql = "SELECT * FROM $TABLE_USERS"

        val statement = connection.prepareStatement(sql)
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val user = UserResult(
                resultSet.getInt(FIELD_ID),
                resultSet.getString(FIELD_FIRST_NAME),
                resultSet.getString(FIELD_LAST_NAME),
                resultSet.getString(FIELD_EMAIL),
                resultSet.getInt(FIELD_ADMIN) == 1
            )
            userList.add(user)
        }
        return userList
    }

    @Throws(SQLException::class)
    fun getUser(token: String): UserResult {
        val sql = "SELECT * FROM $TABLE_USERS WHERE $FIELD_TOKEN = ?"

        val statement = connection.prepareStatement(sql)
        statement.setString(1, token)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            return UserResult(
                resultSet.getInt(FIELD_ID),
                resultSet.getString(FIELD_FIRST_NAME),
                resultSet.getString(FIELD_LAST_NAME),
                resultSet.getString(FIELD_EMAIL),
                resultSet.getInt(FIELD_ADMIN) == 1,
                resultSet.getString(FIELD_TOKEN)
            )
        }

        throw SQLException("Failed to find user for token")
    }

    @Throws(SQLException::class)
    fun addArticle(name: String, brand: String?, quantity: Int, shelf: String): Int {
        val sql =
            "INSERT INTO $TABLE_ARTICLES($FIELD_NAME, $FIELD_BRAND, $FIELD_QUANTITY, $FIELD_LAST_CHANGE, $FIELD_SHELF) VALUES (?, ?, ?, ?, ?)"
        val statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

        statement.setString(1, name)
        statement.setString(2, brand)
        statement.setInt(3, quantity)
        // TODO maybe add this date thigny as part of the query directly, though that might be different between pgsql sqlite
        statement.setDate(4, Date(java.util.Date().time))
        statement.setString(5, shelf)

        statement.executeUpdate()
        val result = statement.generatedKeys
        result.next()
        return result.getInt(1)
    }

    @Throws(SQLException::class)
    fun getAllArticles(): List<ArticleResult> {
        val articlesList = ArrayList<ArticleResult>()

        val sql = "SELECT * FROM $TABLE_ARTICLES"

        val statement = connection.prepareStatement(sql)
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val article = ArticleResult(
                id = resultSet.getInt(FIELD_ID),
                name = resultSet.getString(FIELD_NAME),
                brand = resultSet.getString(FIELD_BRAND),
                quantity = resultSet.getInt(FIELD_QUANTITY),
                last_change = resultSet.getDate(FIELD_LAST_CHANGE),
                shelf = resultSet.getString(FIELD_SHELF)
            )
            articlesList.add(article)
        }
        return articlesList
    }

    @Throws(Exception::class)
    fun login(username: String, password: String): UserResult {
        val sql = "SELECT $FIELD_ID, $FIELD_PASSWORD FROM $TABLE_USERS WHERE $FIELD_EMAIL = ?"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, username)
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            if (BCrypt.checkpw(password, resultSet.getString("password"))) {
                val token = addNewTokenForUse(resultSet.getInt(FIELD_ID))
                return getUser(token)
            }
        }
        throw AuthenticationException()
    }

    @Throws(Exception::class)
    fun logout(token: String) {
        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_TOKEN = ?"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, null)
        statement.setString(2, token)
        if (statement.executeUpdate() == 0) {
            // Nothing was updated, so token must not have been valid
            throw AuthenticationException()
        }
    }

    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean {
        val sql = "SELECT $FIELD_ADMIN FROM $TABLE_USERS WHERE $FIELD_TOKEN = ?"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, token)
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            return resultSet.getInt(FIELD_ADMIN) == 1
        }
        // No results found, so token is wrong
        throw AuthenticationException()
    }

    @Throws(SQLException::class)
    private fun addNewTokenForUse(id: Int): String {
        val token = UUID.randomUUID()

        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_ID = ?"

        val statement = connection.prepareStatement(sql)
        statement.setString(1, token.toString())
        statement.setInt(2, id)

        // Update failed on 0
        if (statement.executeUpdate() == 0) {
            throw SQLException("Failed to update table, check log")
        }
        return token.toString()
    }

    @Throws(SQLException::class)
    protected fun createInitialTables() {
        val dropStatement = connection.createStatement()
        // TODO clean this up, should drop all tables, now just what we specify
        try {
            dropStatement.execute("DROP TABLE $TABLE_USERS, $TABLE_ARTICLES")
            // Catching Excerption, since multi exception not supported on Kotlin yet, also should clean this code
        } catch (e: Exception) {
            // TODO just ignoring does not exist error, entire thingy should be cleaned anyway, fix then
        }
        val file = when (configuration.type) {
            "sqlite" -> ResourceUtils.getFile("classpath:database-layout-sqlite.sql")
            "pgsql" -> ResourceUtils.getFile("classpath:database-layout-pgsql.sql")
            // TODO probably change this to different exception, maybe customn
            else -> throw SQLException("Unknown database configuration")
        }

        // TODO, maybe rethink this. Kinda annoything how it doens't support just executing entire file
        for (s in file.readText(Charsets.UTF_8).split(";")) {
            if (!s.contains("CREATE TABLE")) {
                continue
            }
            connection.createStatement().execute(s)
        }
    }
}