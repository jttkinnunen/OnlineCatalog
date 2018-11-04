package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.components.EmailHandler
import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.mappers.ArticlesMapper
import com.fcgtalent.fcgcatalog.database.mappers.ArticlesMapperNullDescription
import com.fcgtalent.fcgcatalog.database.mappers.AuthenticateTokenMapper
import com.fcgtalent.fcgcatalog.database.mappers.LocationResultMapper
import com.fcgtalent.fcgcatalog.database.mappers.LoginMapper
import com.fcgtalent.fcgcatalog.database.mappers.UserResultMapper
import com.fcgtalent.fcgcatalog.util.ArticleResult
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.LocationResult
import com.fcgtalent.fcgcatalog.util.UserResult
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.ResourceUtils
import java.sql.SQLException
import java.util.UUID
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Timestamp

// TODO explain why twice, or dont
@Repository
class DatabaseConnector(
    private val configuration: DatabaseConfiguration,
    private val jdbcTemplate: JdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val emailHandler: EmailHandler
) {

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
        const val FIELD_RESET_TOKEN = "reset_token"

        // Article fields
        const val FIELD_ARTICLE_NAME = "article_name"
        const val FIELD_IMAGE = "image"
        const val FIELD_LAST_CHANGE = "last_change"
        const val FIELD_DESCRIPTION = "description"

        // Location fiels
        const val FIELD_LOCATION_NAME = "location_name"
        const val FIELD_QUANTITY = "quantity"
        const val FIELD_LOCATION_ID = "location_id"
        const val FIELD_ARTICLE_ID = "article_id"
    }

    init {
        // TODO rethink this, this is just initial testing. But rethink the intiial table geneartion
        dropAllTables()
        createInitialTables()
        addUser("antti", "pantti", "test2", "elefantti@hiano.fi", true)
        addUser("Joni", "Laitala", "test1", "joni.laitala@gmail.com", true)
        addUser("Erkki", "Esimerkki", "test3", "erkki.esimerkki@fcg.fi", true)
        addLocation("Oulu")
        addLocation("Helsinki")
        addLocation("Kiutaköngäs")
        addArticle("Paita, Musta, FCGTalent, L", "/tshirt.png", "Unisex paita. Kestaa tuulta, sadetta ja luoteja")
        addArticle("Paita, Musta, FCGTalent, XL", "/tshirt.png", "Unisex paita. Kestaa tuulta, sadetta ja luoteja")
        addArticle(
            "Kynä, punainen, FCGTalent",
            "ballpenred.png",
            "Kuulakärkikynä jonka muste ei kuivu tai lopu koskaan"
        )
        addArticle(
            "Kynä, sininen, FCGTalent",
            "ballpenblue.png",
            "Kuulakärkikynä jonka muste ei kuivu tai lopu koskaan"
        )
        addArticle(
            "Vihko, kovakantinen, FCGTalent",
            "notepad.png",
            "Kovakantinen ruutusivullinen vihko, jonka kannesa FCG Talent logo. Lorem ipsum dolor sit amet."
        )
        addArticle(
            "Karkkirasia",
            "candybox.png",
            "Pieni rasia hedelmäkarkkeja FCG Talent logolla. Lorem ipsum dolor sit amet."
        )
        addArticle(
            "Purukumi, Laatikko",
            "gum.png",
            "Iso laatikko Jenkki xylitol purukumia. Lorem ipsum dolor sit amet."
        )
        addArticle("Kirjanmerkki, FCG", "bookmark.png", "Kirjanmerkki. Lorem ipsum dolor sit amet.")

        setArticlesAtLocation(1, 1, 25)
        setArticlesAtLocation(1, 2, 52)
        setArticlesAtLocation(2, 2, 12)
        setArticlesAtLocation(2, 1, 0)
        setArticlesAtLocation(1, 3, 2)
        setArticlesAtLocation(2, 4, 20)
        setArticlesAtLocation(3, 4, 150)
        setArticlesAtLocation(1, 4, 0)
        setArticlesAtLocation(1, 5, 0)
        setArticlesAtLocation(1, 6, 4)
        setArticlesAtLocation(1, 7, 3)
        setArticlesAtLocation(1, 8, 17)
    }

    @Throws(SQLException::class)
    fun addUser(firstName: String, lastName: String, email: String, admin: Boolean, id: Int? = null) {
        val sql =
            "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_EMAIL, $FIELD_ADMIN, $FIELD_RESET_TOKEN) VALUES (?, ?, ?, ?, ?)"
        val resetToken = UUID.randomUUID().toString()
        if (jdbcTemplate.update(
                sql,
                firstName,
                lastName,
                email,
                if (admin) 1 else 0,
                resetToken
            ) == 1
        ) {
            emailHandler.sendActivateAccount(email, resetToken)
        }
    }

    // This is for debug useasge, so that we don't neeed to deal with activateion always
    @Throws(SQLException::class)
    fun addUser(firstName: String, lastName: String, password: String, email: String, admin: Boolean, id: Int? = null) {
        val sql =
            "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_PASSWORD, $FIELD_EMAIL, $FIELD_ADMIN) VALUES (?, ?, ?, ?, ?)"
        jdbcTemplate.update(
            sql,
            firstName,
            lastName,
            password,
            email,
            if (admin) 1 else 0
        )
    }

    @Throws(SQLException::class)
    fun updateUser(id: Int, firstName: String, lastName: String, email: String, admin: Boolean) {
        val sql =
            "UPDATE $TABLE_USERS SET $FIELD_FIRST_NAME = ?, $FIELD_LAST_NAME = ?, $FIELD_EMAIL = ?, $FIELD_ADMIN = ? WHERE $FIELD_ID = ?"

        jdbcTemplate.update(
            sql,
            firstName,
            lastName,
            email,
            if (admin) 1 else 0,
            id
        )
    }

    @Throws(SQLException::class)
    fun getUsers(ids: List<Int>): List<UserResult> {
        var sql = "SELECT * FROM $TABLE_USERS"

        if (ids.isEmpty()) {
            return jdbcTemplate.query(sql, UserResultMapper())
        } else {
            // Need do build the statement manually, since sqlite doesn't support setArray
            val parameters = MapSqlParameterSource()
            parameters.addValue("ids", ids)
            sql += " WHERE id IN (:ids)"
            return namedParameterJdbcTemplate.query(sql, parameters, UserResultMapper())
        }
    }

    @Throws(SQLException::class)
    fun getUserWithToken(token: String): UserResult {
        val sql = "SELECT * FROM $TABLE_USERS WHERE $FIELD_TOKEN = ?"
        val result = jdbcTemplate.query(sql, arrayOf<Any>(token), UserResultMapper())
        return result[0]
    }

    @Throws(SQLException::class)
    fun addArticle(name: String, image: String?, description: String) {
        val sql =
            "INSERT INTO $TABLE_ARTICLES($FIELD_ARTICLE_NAME, $FIELD_IMAGE, $FIELD_LAST_CHANGE, $FIELD_DESCRIPTION) VALUES (?, ?, ?, ?)"

        jdbcTemplate.update(sql, name, image, Timestamp(System.currentTimeMillis()), description)
    }

    @Throws(SQLException::class)
    fun updateArticle(id: Int, name: String, image: String?, description: String) {
        val sql =
            "UPDATE $TABLE_ARTICLES SET $FIELD_ARTICLE_NAME = ?, $FIELD_IMAGE = ?, $FIELD_LAST_CHANGE = ?, $FIELD_DESCRIPTION = ? WHERE $FIELD_ID = ?"

        jdbcTemplate.update(sql, name, image, Timestamp(System.currentTimeMillis()), description, id)
    }

    @Throws(SQLException::class)
    fun getArticles(
        articleIds: List<Int>,
        locationIds: List<Int>,
        includeDescription: Boolean = false
    ): List<ArticleResult> {
        // TODO improve this fucking stupid array thingy roska moska
        val arrayThingy1: String
        val arrayThingy2: String
        val arrayThingy3: String

        // TODO clean this alias stuff? MNove it to the largtet statemenet?
        if (configuration.type == "sqlite") {
            arrayThingy1 = "GROUP_CONCAT($FIELD_LOCATION_ID) AS $FIELD_LOCATION_ID"
            arrayThingy2 = "GROUP_CONCAT($FIELD_LOCATION_NAME) AS $FIELD_LOCATION_NAME"
            arrayThingy3 = "GROUP_CONCAT($FIELD_QUANTITY) AS $FIELD_QUANTITY"
        } else {
            arrayThingy1 = "string_agg($FIELD_LOCATION_ID::character varying, ',') AS $FIELD_LOCATION_ID"
            arrayThingy2 = "string_agg($FIELD_LOCATION_NAME, ',') AS $FIELD_LOCATION_NAME"
            arrayThingy3 = "string_agg($FIELD_QUANTITY::character varying, ',') AS $FIELD_QUANTITY"

        }

        var sql =
            "SELECT $arrayThingy1, $TABLE_ARTICLES.$FIELD_ID AS $FIELD_ARTICLE_ID, $arrayThingy2, $arrayThingy3, $FIELD_ARTICLE_NAME, $FIELD_IMAGE, $FIELD_LAST_CHANGE, $FIELD_DESCRIPTION " +
                "FROM $TABLE_ARTICLES LEFT JOIN $TABLE_ARTICLE_LOCATIONS ON $TABLE_ARTICLES.$FIELD_ID = $TABLE_ARTICLE_LOCATIONS.$FIELD_ARTICLE_ID " +
                "LEFT JOIN $TABLE_LOCATION ON $TABLE_LOCATION.$FIELD_ID = $TABLE_ARTICLE_LOCATIONS.$FIELD_LOCATION_ID"

        val result: List<ArticleResult>

        val parameters = MapSqlParameterSource()
        if (locationIds.isNotEmpty() && articleIds.isNotEmpty()) {
            sql += " WHERE $TABLE_ARTICLES.$FIELD_ID IN (:articleIds) AND $TABLE_LOCATION.$FIELD_ID IN (:locationIds)"
            parameters.addValue("articleIds", articleIds)
            parameters.addValue("locationIds", locationIds)
        } else if (locationIds.isNotEmpty()) {
            sql += " WHERE $TABLE_LOCATION.$FIELD_ID IN (:locationIds)"
            parameters.addValue("locationIds", locationIds)
        } else if (articleIds.isNotEmpty()) {
            sql += " WHERE $TABLE_ARTICLES.$FIELD_ID IN (:articleIds)"
            parameters.addValue("articleIds", articleIds)
        }
        sql += " GROUP BY $TABLE_ARTICLES.$FIELD_ID, $FIELD_ARTICLE_NAME, $FIELD_IMAGE, $FIELD_LAST_CHANGE, $FIELD_DESCRIPTION"

        result = namedParameterJdbcTemplate.query(
            sql,
            parameters,
            if (includeDescription) ArticlesMapper() else ArticlesMapperNullDescription()
        )

        return result
    }

    @Throws(SQLException::class)
    fun setArticlesAtLocation(locationId: Int, articleId: Int, quantity: Int) {

        // TODO rethink how to handle this typ eof issues
        val sql = when (configuration.type) {
            "sqlite" -> "INSERT OR REPLACE INTO $TABLE_ARTICLE_LOCATIONS($FIELD_LOCATION_ID, $FIELD_ARTICLE_ID, $FIELD_QUANTITY) VALUES (?, ?, ?)"
            "pgsql" -> "INSERT INTO $TABLE_ARTICLE_LOCATIONS($FIELD_LOCATION_ID, $FIELD_ARTICLE_ID, $FIELD_QUANTITY) VALUES (?, ?, ?) " +
                "ON CONFLICT ($FIELD_LOCATION_ID, $FIELD_ARTICLE_ID) DO UPDATE SET $FIELD_QUANTITY = excluded.$FIELD_QUANTITY"
            else -> throw Exception("Unsupported DB type")
        }

        jdbcTemplate.update(sql, locationId, articleId, quantity)
    }

    @Throws(SQLException::class)
    fun addLocation(name: String) {
        val sql = "INSERT INTO $TABLE_LOCATION($FIELD_LOCATION_NAME) VALUES (?)"
        jdbcTemplate.update(sql, name)
    }

    @Throws(SQLException::class)
    fun updateLocation(id: Int, name: String) {
        val sql = "UPDATE $TABLE_LOCATION SET $FIELD_LOCATION_NAME = ? WHERE $FIELD_ID = ?"
        jdbcTemplate.update(sql, name, id)
    }

    @Throws(SQLException::class)
    fun getLocations(ids: List<Int>): List<LocationResult> {
        var sql = "SELECT * FROM $TABLE_LOCATION"

        if (ids.isEmpty()) {
            return jdbcTemplate.query(sql, LocationResultMapper())
        } else {
            // Need do build the statement manually, since sqlite doesn't support setArray
            val parameters = MapSqlParameterSource()
            parameters.addValue("ids", ids)
            sql += " WHERE id IN (:ids)"
            return namedParameterJdbcTemplate.query(sql, parameters, LocationResultMapper())
        }
    }

    @Throws(Exception::class)
    fun login(username: String, password: String): UserResult {
        val sql = "SELECT $FIELD_ID, $FIELD_PASSWORD FROM $TABLE_USERS WHERE $FIELD_EMAIL = ?"
        val user = jdbcTemplate.query(sql, arrayOf<Any>(username), LoginMapper())
        if (user.isNotEmpty()) {
            if (BCrypt.checkpw(password, user[0].password)) {

                val token = addNewTokenForUse(user[0].id)

                return getUserWithToken(token)
            }
        }
        throw AuthenticationException()
    }

    @Throws(Exception::class)
    fun logout(token: String) {
        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_TOKEN = ?"
        if (jdbcTemplate.update(sql, null, token) == 0) {
            throw AuthenticationException()
        }
    }

    @Throws(Exception::class)
    fun initiatePasswordReset(email: String) {
        val sql = "UPDATE $TABLE_USERS SET $FIELD_RESET_TOKEN = ? WHERE $FIELD_EMAIL = ?"
        val resetToken = UUID.randomUUID().toString()
        if (jdbcTemplate.update(sql, resetToken, email) == 1) {
            emailHandler.sendPasswordReset(email, resetToken)
        } // TODO throw error
    }

    @Throws(Exception::class)
    fun setPassword(resetToken: String, newPassword: String) {
        val sql = "UPDATE $TABLE_USERS SET $FIELD_PASSWORD = ?, $FIELD_RESET_TOKEN = ? WHERE $FIELD_RESET_TOKEN = ?"
        if (jdbcTemplate.update(sql, BCrypt.hashpw(newPassword, BCrypt.gensalt(4)), null, resetToken) == 0) {
            throw AuthenticationException()
        }
    }

    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean {
        val sql = "SELECT $FIELD_ADMIN FROM $TABLE_USERS WHERE $FIELD_TOKEN = ?"

        val admin = jdbcTemplate.query(sql, arrayOf<Any>(token), AuthenticateTokenMapper())
        if (admin.isNotEmpty()) {
            return admin[0]
        }
        // No results found, so token is wrong
        throw AuthenticationException()
    }

    @Throws(SQLException::class)
    private fun addNewTokenForUse(id: Int): String {
        val token = UUID.randomUUID()

        val sql = "UPDATE $TABLE_USERS SET $FIELD_TOKEN = ? WHERE $FIELD_ID = ?"
        jdbcTemplate.update(sql, token, id)
        return token.toString()
    }

    @Throws(SQLException::class)
    fun dropAllTables() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS $TABLE_ARTICLE_LOCATIONS")

        jdbcTemplate.execute("DROP TABLE IF EXISTS $TABLE_USERS")
        jdbcTemplate.execute("DROP TABLE IF EXISTS $TABLE_ARTICLES")
        jdbcTemplate.execute("DROP TABLE IF EXISTS $TABLE_LOCATION")
    }

    @Throws(SQLException::class)
    fun createInitialTables() {
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
            jdbcTemplate.execute(s)
        }
    }
}