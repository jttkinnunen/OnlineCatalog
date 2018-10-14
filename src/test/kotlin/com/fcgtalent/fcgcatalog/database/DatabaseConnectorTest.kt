package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.IntegrationTests
import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ADMIN
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_EMAIL
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_FIRST_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_TOKEN
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.util.UUID

/**
 * Parametirized test class. Runs tests for both PgSQL and SQLite.
 * I predict with my crystal ball that this will go really badly. Since I cannot run the tests seperately (easily)
 * But lets see how it goes and what can be learned from it.
 */
@Category(IntegrationTests::class)
@RunWith(Parameterized::class)
class DatabaseConnectorTest(
    private val name: String,
    private val configuration: DatabaseConfiguration
) {

    private lateinit var databaseHandler: DatabaseHandler

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0}")
        fun data(): List<Array<Any>> {
            val sqliteConfiguration: DatabaseConfiguration = mockk()
            every { sqliteConfiguration.sqlitememory } returns true
            every { sqliteConfiguration.type } returns "sqlite"

            val pgsqlConfiguration: DatabaseConfiguration = mockk()
            // TODO Add support for pgsql
            every { pgsqlConfiguration.type } returns "pgsql"
            // TODO do proper connection to database for tests
            every { pgsqlConfiguration.address } returns "192.168.1.129"
            every { pgsqlConfiguration.port } returns "5432"
            every { pgsqlConfiguration.name } returns "fcgcatalog"
            every { pgsqlConfiguration.username } returns "postgres"
            every { pgsqlConfiguration.password } returns "postgres"

            return listOf(arrayOf("SQLite", sqliteConfiguration),
                    arrayOf("PgSQL", pgsqlConfiguration))
        }

        // User info placed to database for testing
        private const val firstName1 = "Moo1"
        private const val lastName1 = "MOoo2"
        private const val password1 = "hiano"
        private const val email1 = "joo@koo.com"
        private const val admin1 = true

        private const val firstName2 = "Mofweo1"
        private const val lastName2 = "MOfweoo2"
        private const val password2 = "hiawefno"
        private const val email2 = "joowefwe@fwefwe.org"
        private const val admin2 = false
    }

    @Before
    fun setUp() {
        databaseHandler = DatabaseHandler(configuration)
        addTestUsers()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testAddUser_GetUsers() {
        val results = databaseHandler.getAllUsers()

        // This test could fail, because the results could come in a different order?
        Assert.assertThat(results.length(), `is`(2))
        val firstResult = results[0] as JSONObject
        Assert.assertThat(firstResult.getString(FIELD_FIRST_NAME), `is`(firstName1))
        Assert.assertThat(firstResult.getString(FIELD_LAST_NAME), `is`(lastName1))
        Assert.assertThat(firstResult.getString(FIELD_EMAIL), `is`(email1))
        Assert.assertThat(firstResult.getBoolean(FIELD_ADMIN), `is`(admin1))

        val secondResult = results[1] as JSONObject
        Assert.assertThat(secondResult.getString(FIELD_FIRST_NAME), `is`(firstName2))
        Assert.assertThat(secondResult.getString(FIELD_LAST_NAME), `is`(lastName2))
        Assert.assertThat(secondResult.getString(FIELD_EMAIL), `is`(email2))
        Assert.assertThat(secondResult.getBoolean(FIELD_ADMIN), `is`(admin2))
    }

    @Test
    fun testLogin_success() {
        val result: JSONObject = databaseHandler.login(email1, password1)
        Assert.assertTrue(UUID.fromString(result.getString(FIELD_TOKEN)) != null)
    }

    @Test
    fun testLogin_failure() {
        try {
            databaseHandler.login(email1, password2)
            Assert.fail()
        } catch (e: AuthenticationException) { }
    }

    @Test
    fun testAuthenticateToken_success() {
        // Check one with admin privileges
        val result1: JSONObject = databaseHandler.login(email1, password1)
        val token1 = result1.getString(FIELD_TOKEN)
        Assert.assertThat(databaseHandler.authenticateToken(token1), `is`(admin1))

        // Check one without admin privleges
        val result2: JSONObject = databaseHandler.login(email2, password2)
        val token2 = result2.getString(FIELD_TOKEN)

        Assert.assertThat(databaseHandler.authenticateToken(token2), `is`(admin2))
    }

    @Test
    fun testAuthenticateToken_failure() {
        try {
            databaseHandler.authenticateToken("39e1c6a4-a206-48cd-8b17-6c94104dc421")
            Assert.fail()
        } catch (e: AuthenticationException) { }
    }

    @Test
    fun testLogout_success() {
        // Check one with admin privileges
        val result1: JSONObject = databaseHandler.login(email1, password1)
        val token1 = result1.getString(FIELD_TOKEN)
        databaseHandler.logout(token1)

        // We have logged out so we should fail to authenticate
        try {
            databaseHandler.authenticateToken(token1)
            Assert.fail()
        } catch (e: AuthenticationException) { }
    }

    @Test
    fun testLogout_failure() {
        // We are not logged in, so we should fail to logout
        try {
            databaseHandler.logout("39e1c6a4-a206-48cd-8b17-6c94104dc421")
            Assert.fail()
        } catch (e: AuthenticationException) { }
    }

    @Test
    fun testAddArticle() {
        val name1 = "moo1"
        val brand1 = "moo2"
        val count1 = 1
        val shelf1 = "korkein"

        val name2 = "moo1"
        val brand2 = "moo2"
        val count2 = 1
        val shelf2 = "korkein"

        Assert.assertThat(databaseHandler.addArticle(name1, brand1, count1, shelf1), `is`(1))

        Assert.assertThat(databaseHandler.addArticle(name2, brand2, count2, shelf2), `is`(2))
    }

    private fun addTestUsers() {
        databaseHandler.addUser(firstName1, lastName1, password1, email1, admin1)
        databaseHandler.addUser(firstName2, lastName2, password2, email2, admin2)
    }
}
