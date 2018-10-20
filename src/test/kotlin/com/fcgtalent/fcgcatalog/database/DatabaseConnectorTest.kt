package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.IntegrationTests
import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.UserResult
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

            return listOf(
                arrayOf("SQLite", sqliteConfiguration),
                arrayOf("PgSQL", pgsqlConfiguration)
            )
        }

        // User info placed to database for testing
        //  private val addUserBody1 = AddUserBody("Moo1", "Moo1", "hiano", "joo@koo.com", true, null)
        private const val firstName1 = "Moo1"
        private const val lastName1 = "MOoo2"
        private const val password1 = "hiano"
        private const val email1 = "joo@koo.com"
        private const val admin1 = true
        //    private val addUserBody2 = AddUserBody("Mofweo1", "MOfweoo2", "hiawefno", "joowefwe@fwefwe.org", false, null)

        private const val firstName2 = "Mofweo1"
        private const val lastName2 = "MOfweoo2"
        private const val password2 = "hiawefno"
        private const val email2 = "joowefwe@fwefwe.org"
        private const val admin2 = false

        // private val addArticlesBody = AddArticleBody("moo1", "moo2", 1, "korkein", null)

        // Article related info, used for testing
        private const val name1 = "moo1"
        private const val brand1 = "moo2"
        private const val count1 = 1
        private const val shelf1 = "korkein"

        private const val name2 = "moo1"
        private const val brand2 = "moo2"
        private const val count2 = 3
        private const val shelf2 = "korkein"
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
    fun testAddUser_GetUsers_success() {
        val results = databaseHandler.getUsers(listOf())

        // This test could fail, because the results could come in a different order?
        Assert.assertThat(results.size, `is`(2))
        val firstResult = results[0] as UserResult
        Assert.assertThat(firstResult.firstName, `is`(firstName1))
        Assert.assertThat(firstResult.lastName, `is`(lastName1))
        Assert.assertThat(firstResult.email, `is`(email1))
        Assert.assertThat(firstResult.admin, `is`(admin1))

        val secondResult = results[1] as UserResult
        Assert.assertThat(secondResult.firstName, `is`(firstName2))
        Assert.assertThat(secondResult.lastName, `is`(lastName2))
        Assert.assertThat(secondResult.email, `is`(email2))
        Assert.assertThat(secondResult.admin, `is`(admin2))
    }

    @Test
    fun testGetUsers() {
        var results = databaseHandler.getUsers(listOf(1, 2))
        Assert.assertThat(results.size, `is`(2))

        results = databaseHandler.getUsers(listOf(1, 3))
        Assert.assertThat(results.size, `is`(1))

        results = databaseHandler.getUsers(listOf(1, 2, 5, 7, 8))
        Assert.assertThat(results.size, `is`(2))

        results = databaseHandler.getUsers(listOf(20, 11))
        Assert.assertThat(results.size, `is`(0))

        results = databaseHandler.getUsers(listOf(1, 2))
        Assert.assertThat(results.size, `is`(2))

    }

    @Test
    fun testLogin_success() {
        val result: UserResult = databaseHandler.login(email1, password1)
        Assert.assertTrue(UUID.fromString(result.token) != null)
        Assert.assertThat(result.admin, `is`(admin1))
        Assert.assertThat(result.email, `is`(email1))
        Assert.assertThat(result.firstName, `is`(firstName1))
        Assert.assertThat(result.lastName, `is`(lastName1))
    }

    @Test
    fun testLogin_failure() {
        try {
            databaseHandler.login(email1, password2)
            Assert.fail()
        } catch (e: AuthenticationException) {
        }
    }

    @Test
    fun testAuthenticateToken_success() {
        // Check one with admin privileges
        val result1: UserResult = databaseHandler.login(email1, password1)
        val token1 = result1.token
        Assert.assertThat(databaseHandler.authenticateToken(token1!!), `is`(admin1))

        // Check one without admin privleges
        val result2: UserResult = databaseHandler.login(email2, password2)
        val token2 = result2.token

        Assert.assertThat(databaseHandler.authenticateToken(token2!!), `is`(admin2))
    }

    @Test
    fun testAuthenticateToken_failure() {
        try {
            databaseHandler.authenticateToken("39e1c6a4-a206-48cd-8b17-6c94104dc421")
            Assert.fail()
        } catch (e: AuthenticationException) {
        }
    }

    @Test
    fun testLogout_success() {
        // Check one with admin privileges
        val result1: UserResult = databaseHandler.login(email1, password1)
        val token1 = result1.token
        databaseHandler.logout(token1!!)

        // We have logged out so we should fail to authenticate
        try {
            databaseHandler.authenticateToken(token1)
            Assert.fail()
        } catch (e: AuthenticationException) {
        }
    }

    @Test
    fun testLogout_failure() {
        // We are not logged in, so we should fail to logout
        try {
            databaseHandler.logout("39e1c6a4-a206-48cd-8b17-6c94104dc421")
            Assert.fail()
        } catch (e: AuthenticationException) {
        }
    }

    @Test
    fun testAddArticle_success() {
        Assert.assertThat(databaseHandler.addArticle(name1, brand1, count1, shelf1), `is`(1))
        Assert.assertThat(databaseHandler.addArticle(name2, brand2, count2, shelf2), `is`(2))
    }

    @Test
    fun testGetArticles() {
        addTestArticles()

        val results = databaseHandler.getAllArticles()
        Assert.assertThat(results.size, `is`(2))
        val firstResult = results[0]
        println(firstResult.toString())
        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(name1))
        Assert.assertThat(firstResult.brand, `is`(brand1))
        Assert.assertThat(firstResult.quantity, `is`(count1))
        Assert.assertThat(firstResult.shelf, `is`(shelf1))

        val secondResult = results[1]
        Assert.assertThat(secondResult.id, `is`(2))
        Assert.assertThat(secondResult.name, `is`(name2))
        Assert.assertThat(secondResult.brand, `is`(brand2))
        Assert.assertThat(secondResult.quantity, `is`(count2))
        Assert.assertThat(secondResult.shelf, `is`(shelf2))
    }

    private fun addTestUsers() {
        databaseHandler.addUser(firstName1, lastName1, password1, email1, admin1)
        databaseHandler.addUser(firstName2, lastName2, password2, email2, admin2)
    }

    private fun addTestArticles() {
        databaseHandler.addArticle(name1, brand1, count1, shelf1)
        databaseHandler.addArticle(name2, brand2, count2, shelf2)
    }
}
