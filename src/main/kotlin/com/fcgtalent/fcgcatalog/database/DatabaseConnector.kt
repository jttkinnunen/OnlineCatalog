package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.mappers.ArticleResultMapper
import com.fcgtalent.fcgcatalog.database.mappers.ArticlesInLocationsMapper
import com.fcgtalent.fcgcatalog.database.mappers.AuthenticateTokenMapper
import com.fcgtalent.fcgcatalog.database.mappers.LocationResultMapper
import com.fcgtalent.fcgcatalog.database.mappers.LoginMapper
import com.fcgtalent.fcgcatalog.database.mappers.UserResultMapper
import com.fcgtalent.fcgcatalog.util.ArticleResult
import com.fcgtalent.fcgcatalog.util.ArticlesInLocationsResult
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.LocationQuantityResult
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
    protected val configuration: DatabaseConfiguration,
    private val jdbcTemplate: JdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
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
        addUser("antti", "pantti", "hiano", "elefantti@hiano.fi", true)
        addUser("Joni", "Laitala", "2479", "joni.laitala@gmail.com", true)
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
    fun addUser(firstName: String, lastName: String, password: String, email: String, admin: Boolean, id: Int? = null) {
        val sql =
            "INSERT INTO $TABLE_USERS($FIELD_FIRST_NAME, $FIELD_LAST_NAME, $FIELD_PASSWORD, $FIELD_EMAIL, $FIELD_ADMIN) VALUES (?, ?, ?, ?, ?)"

        jdbcTemplate.update(
            sql,
            firstName,
            lastName,
            BCrypt.hashpw(password, BCrypt.gensalt(4)),
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
    fun getArticles(articleIds: List<Int>): List<ArticleResult> {
        var sql = "SELECT * FROM $TABLE_ARTICLES"

        if (articleIds.isEmpty()) {
            return jdbcTemplate.query(sql, ArticleResultMapper())
        } else {
            // Need do build the statement manually, since sqlite doesn't support setArray
            val parameters = MapSqlParameterSource()
            parameters.addValue("ids", articleIds)
            sql += " WHERE id IN (:ids)"
            return namedParameterJdbcTemplate.query(sql, parameters, ArticleResultMapper())
        }
    }

    /**
     * Damn this is ugly, but what can you do.. with having to use inner join and manually adding WHERE IN
     */
    @Throws(SQLException::class)
    fun getArticlesInLocations(articleIds: List<Int>, locationIds: List<Int>): List<ArticleResult> {
        var sql =
            "SELECT * FROM $TABLE_ARTICLE_LOCATIONS INNER JOIN $TABLE_ARTICLES ON $TABLE_ARTICLES.$FIELD_ID = " +
                "$TABLE_ARTICLE_LOCATIONS.$FIELD_ARTICLE_ID INNER JOIN $TABLE_LOCATION ON $TABLE_LOCATION.$FIELD_ID = " +
                "$TABLE_ARTICLE_LOCATIONS.$FIELD_LOCATION_ID"

        val result: List<ArticlesInLocationsResult>
        // TODO clean this if mess up a little
        if (locationIds.isNotEmpty() && articleIds.isNotEmpty()) {
            sql += " WHERE $TABLE_ARTICLES.$FIELD_ID IN (:articleIds) AND $TABLE_LOCATION.$FIELD_ID IN (:locationIds)"
            val parameters = MapSqlParameterSource()
            parameters.addValue("articleIds", articleIds)
            parameters.addValue("locationIds", locationIds)
            result = namedParameterJdbcTemplate.query(sql, parameters, ArticlesInLocationsMapper())
        } else if (locationIds.isNotEmpty()) {
            sql += " WHERE $TABLE_LOCATION.$FIELD_ID IN (:locationIds)"
            val parameters = MapSqlParameterSource()
            parameters.addValue("locationIds", locationIds)
            result = namedParameterJdbcTemplate.query(sql, parameters, ArticlesInLocationsMapper())
        } else if (articleIds.isNotEmpty()) {
            sql += " WHERE $TABLE_ARTICLES.$FIELD_ID IN (:articleIds)"
            val parameters = MapSqlParameterSource()
            parameters.addValue("articleIds", articleIds)
            result = namedParameterJdbcTemplate.query(sql, parameters, ArticlesInLocationsMapper())
        } else {
            result = jdbcTemplate.query(sql, ArticlesInLocationsMapper())
        }

        // TODO improve this and the entire thing. Just an ugly fast way of getting it to spec. Do better later
        val finalResult = HashMap<Int, ArticleResult>()

        for (a in result) {
            if (!finalResult.containsKey(a.id)) {
                finalResult.put(
                    a.id,
                    ArticleResult(
                        a.id,
                        a.name,
                        a.image,
                        a.last_change,
                        null,
                        mutableListOf(LocationQuantityResult(a.locationId, a.locationName, a.quantity))
                    )
                )
            } else {
                finalResult.get(a.id)?.locations!!.add(LocationQuantityResult(a.locationId, a.locationName, a.quantity))
            }
        }

        return finalResult.values.toMutableList()
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