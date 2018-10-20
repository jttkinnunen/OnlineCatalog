package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.ArticleResult
import com.fcgtalent.fcgcatalog.util.ArticlesInLocationsResult
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.LocationResult
import com.fcgtalent.fcgcatalog.util.UserResult
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import javax.sql.DataSource




// TODO maybe clean this kinda like how the REST interface was cleaned up a bit
abstract class DatabaseConnector(protected val configuration: DatabaseConfiguration) {

    protected abstract val connection: Connection

    companion object {
        const val TABLE_USERS = "users"
        const val TABLE_ARTICLES = "articles"
        const val TABLE_LOCATION = "location"
        const val TABLE_ARTICLE_LOCATIONS = "article_locations"

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
        const val FIELD_LAST_CHANGE = "last_change"
        const val FIELD_SHELF = "shelf"

        // Location fiels
        const val FIELD_QUANTITY = "quantity"

    }


    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    @Throws(SQLException::class)
    fun addUser(firstName: String, lastName: String, password: String, email: String, admin: Boolean) {
        val sql =
            "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_PASSWORD, $FIELD_EMAIL, $FIELD_ADMIN) VALUES (?, ?, ?, ?, ?)"

//        val statement = connection.prepareStatement(sql)
        jdbcTemplate!!.update(sql, arrayOf<Any>(firstName, lastName, BCrypt.hashpw(password, BCrypt.gensalt(4)), email, if (admin) 1 else 0))

//        statement.setString(1, firstName)
//        statement.setString(2, lastName)
//        statement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt(4)))
//        statement.setString(4, email)
//        statement.setInt(5, if (admin) 1 else 0)
//
//        statement.executeUpdate()
    }

    @Throws(SQLException::class)
    fun getUsers(ids: List<Int>): List<UserResult> {
        val usersList = ArrayList<UserResult>()

        val statement = createPreparedStatementForIdList(ids, TABLE_USERS)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            usersList.add(
                UserResult(
                    resultSet.getInt(FIELD_ID),
                    resultSet.getString(FIELD_FIRST_NAME),
                    resultSet.getString(FIELD_LAST_NAME),
                    resultSet.getString(FIELD_EMAIL),
                    resultSet.getInt(FIELD_ADMIN) == 1
                )
            )
        }
        return usersList
    }

    @Throws(SQLException::class)
    fun getUserWithToken(token: String): UserResult {
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

    // TODO move date to timestamp
    @Throws(SQLException::class)
    fun addArticle(name: String, brand: String?, shelf: String) {
        val sql =
            "INSERT INTO $TABLE_ARTICLES($FIELD_NAME, $FIELD_BRAND, $FIELD_LAST_CHANGE, $FIELD_SHELF) VALUES (?, ?, ?, ?)"
        val statement = connection.prepareStatement(sql)

        statement.setString(1, name)
        statement.setString(2, brand)
        // TODO maybe add this date thigny as part of the query directly, though that might be different between pgsql sqlite
        statement.setDate(3, Date(java.util.Date().time))
        statement.setString(4, shelf)

        statement.executeUpdate()
        // TODO remove once you have finished deciding hwo upload is to be fixed
//        val result = statement.generatedKeys
//        result.next()
//        return result.getInt(1)
    }

    @Throws(SQLException::class)
    fun getAllArticles(articleIds: List<Int>): List<ArticleResult> {
        val articlesList = ArrayList<ArticleResult>()

        val statement = createPreparedStatementForIdList(articleIds, TABLE_ARTICLES)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            articlesList.add(
                    ArticleResult(
                        id = resultSet.getInt(FIELD_ID),
                        name = resultSet.getString(FIELD_NAME),
                        brand = resultSet.getString(FIELD_BRAND),
                        last_change = resultSet.getDate(FIELD_LAST_CHANGE),
                        shelf = resultSet.getString(FIELD_SHELF)
                )

            )
        }
        return articlesList
    }

    /**
     * Damn this is ugly, but what can you do.. with having to use inner join and manually adding WHERE IN
     */
    @Throws(SQLException::class)
    fun getArticlesInLocations(articleIds: List<Int>, locationIds: List<Int>): List<ArticlesInLocationsResult> {
        val articlesList = ArrayList<ArticlesInLocationsResult>()


        var sql = "SELECT * FROM article_locations INNER JOIN articles ON articles.id = article_locations.article_id INNER JOIN location ON location.id = article_locations.location_id"

        // Lets try how these inner functions thingys work :S
        fun addIn(ids: List<Int>, tableName: String) {
            sql += " $tableName.id IN ("
            val iterator = ids.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                sql += "?"
                if (iterator.hasNext()) {
                    sql += ","
                }
            }
            sql += ")"
        }

        if(locationIds.isNotEmpty() || articleIds.isNotEmpty()) {
            sql += " WHERE "
            if(locationIds.isNotEmpty()) {
                addIn(locationIds, TABLE_LOCATION)
                // Add and if we are going to still be adding articleIds
                if(articleIds.isNotEmpty()) {
                    sql += " AND "
                }
            }

            if(articleIds.isNotEmpty()) {
                addIn(articleIds, TABLE_ARTICLES)
            }
        }
        println("TODO REMOVE: OUR cool little statement $sql")
        val statement = connection.prepareStatement(sql)
        // Need do build the statement manually, since sqlite doesn't support setArray
        locationIds.forEachIndexed{ index, i -> statement.setInt(index + 1, i) }

        articleIds.forEachIndexed { index, i -> statement.setInt(index + locationIds.size + 1, i) }
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            val article = ArticlesInLocationsResult(
                id = resultSet.getInt("$TABLE_ARTICLES.$FIELD_ID"),
                name = resultSet.getString("$TABLE_ARTICLES.$FIELD_NAME"),
                brand = resultSet.getString("$TABLE_ARTICLES.$FIELD_BRAND"),
                last_change = resultSet.getDate("$TABLE_ARTICLES.$FIELD_LAST_CHANGE"),
                shelf = resultSet.getString("$TABLE_ARTICLES.$FIELD_SHELF"),
                locationId = resultSet.getInt("$TABLE_LOCATION.$FIELD_ID"),
                quantity = resultSet.getInt("$TABLE_LOCATION.$FIELD_QUANTITY")
            )
            articlesList.add(article)
        }
        return articlesList
    }

    @Throws(SQLException::class)
    fun addLocation(name: String) {
        val sql = "INSERT INTO $TABLE_LOCATION($FIELD_NAME) VALUES (?)"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, name)
        statement.executeUpdate()
    }

    @Throws(SQLException::class)
    fun getLocations(ids: List<Int>): List<LocationResult> {
        val locationList = ArrayList<LocationResult>()

        val statement = createPreparedStatementForIdList(ids, TABLE_LOCATION)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            locationList.add(
                LocationResult(
                    resultSet.getInt(FIELD_ID),
                    resultSet.getString(FIELD_NAME)
                )
            )
        }
        return locationList
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
                return getUserWithToken(token)
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
            val admin  = resultSet.getInt(FIELD_ADMIN) == 1
            return admin
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
            dropStatement.execute("DROP TABLE $TABLE_USERS, $TABLE_ARTICLES, $TABLE_LOCATION, $TABLE_ARTICLE_LOCATIONS")
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

    @Throws(SQLException::class)
    private fun createPreparedStatementForIdList(ids: List<Int>, table: String): PreparedStatement {
        var sql: String

        if (ids.isEmpty()) {
            sql = "SELECT * FROM $table"
        } else {
            // Need do build the statement manually, since sqlite doesn't support setArray
            sql = "SELECT * FROM $table WHERE $FIELD_ID IN ("
            val iterator = ids.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                sql += "?"
                if (iterator.hasNext()) {
                    sql += ","
                }
            }
            sql += ")"
        }

        val statement = connection.prepareStatement(sql)
        // Need do build the statement manually, since sqlite doesn't support setArray
        ids.forEachIndexed { index, i -> statement.setInt(index + 1, i) }
        return statement
    }
}