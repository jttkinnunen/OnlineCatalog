package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.springframework.stereotype.Repository
import java.lang.Exception

// TODO move configuration to spring style bean thingy. Or maybe not, easier to mock this way
@Repository
class DatabaseHandler(private val configuration: DatabaseConfiguration) {

    // TODO move get this through Autowire or some other mnannter, shouldn't probably creat einstance
    private val databaseConnector = run { when (configuration.type) {
        "sqlite" -> SQLiteConnector(configuration)
        "pgsql" -> PgSQLConnector(configuration)
        else -> throw RuntimeException("Unsupported database configured.")
    } }

    @Throws(Exception::class)
    fun getUserWithToken(token: String) = databaseConnector.getUserWithToken(token)
    @Throws(Exception::class)
    fun getUsers(ids: List<Int>) = databaseConnector.getUsers(ids)
    @Throws(Exception::class)
    fun addUser(firstName: String, lastName: String, pass: String, email: String, admin: Boolean) =
            databaseConnector.addUser(firstName, lastName, pass, email, admin)
    @Throws(Exception::class)
    fun addArticle(name: String, brand: String?, shelf: String) =
            databaseConnector.addArticle(name, brand, shelf)
    @Throws(Exception::class)
    fun getArticles(ids: List<Int>) = databaseConnector.getAllArticles(ids)
    @Throws(Exception::class)
    fun getArticlesInLocations(ids: List<Int>, locationIds: List<Int>) = databaseConnector.getArticlesInLocations(ids, locationIds)
    @Throws(Exception::class)
    fun addLocation(name: String) = databaseConnector.addLocation(name)
    @Throws(Exception::class)
    fun getLocations(ids: List<Int>) = databaseConnector.getLocations(ids)
    @Throws(Exception::class)
    fun logout(token: String) = databaseConnector.logout(token)
    @Throws(Exception::class)
    fun login(username: String, password: String) = databaseConnector.login(username, password)
    @Throws(Exception::class)
    fun authenticateToken(token: String): Boolean = databaseConnector.authenticateToken(token)
}