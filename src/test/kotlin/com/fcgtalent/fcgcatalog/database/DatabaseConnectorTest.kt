package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.IntegrationTests
import com.fcgtalent.fcgcatalog.components.EmailHandler
import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.util.AuthenticationException
import com.fcgtalent.fcgcatalog.util.UserResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID
import javax.sql.DataSource

/**
 * Parametirized test class. Runs tests for both PgSQL and SQLite.
 * I predict with my crystal ball that this will go really badly. Since I cannot run the tests seperately (easily)
 * But lets see how it goes and what can be learned from it.
 */
@Category(IntegrationTests::class)
@RunWith(Parameterized::class)
class DatabaseConnectorTest(
    private val name: String,
    private val configuration: DatabaseConfiguration,
    private val dataSourceParam: DataSource
) {

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

            val sqliteDataSourceBuilder = DataSourceBuilder.create()
            sqliteDataSourceBuilder.driverClassName("org.sqlite.JDBC")
            sqliteDataSourceBuilder.url("jdbc:sqlite::memory:")
            val sqliteDataSource = sqliteDataSourceBuilder.build()

            val pgsqlDataSourceBuilder = DataSourceBuilder.create()
            pgsqlDataSourceBuilder.driverClassName("org.postgresql.Driver")
            pgsqlDataSourceBuilder.url("jdbc:postgresql://${pgsqlConfiguration.address}:${pgsqlConfiguration.port}/${pgsqlConfiguration.name}")
            pgsqlDataSourceBuilder.username(pgsqlConfiguration.username)
            pgsqlDataSourceBuilder.password(pgsqlConfiguration.password)
            val pgsqlDataSource = pgsqlDataSourceBuilder.build()

            return listOf(
                arrayOf("SQLite", sqliteConfiguration, sqliteDataSource),
                arrayOf("PgSQL", pgsqlConfiguration, pgsqlDataSource)
            )
        }

        // User info placed to database for testing
        private const val firstName1 = "Moo1"
        private const val lastName1 = "MOoo2"
        private const val password1 = "hiano"
        private const val email1 = "joo@koo.com"
        private const val admin1 = true
        private lateinit var resetToken1: String

        private const val firstName2 = "Mofweo1"
        private const val lastName2 = "MOfweoo2"
        private const val password2 = "hiawefno"
        private const val email2 = "joowefwe@fwefwe.org"
        private const val admin2 = false
        private lateinit var resetToken2: String

        // Article related info, used for testing
        private const val name1 = "moo1"
        private const val image1 = "moo2"
        private const val description1 = "korkein"

        private const val name2 = "moo1"
        private const val image2 = "moo2"
        private const val description2 = "korkein"

        // Location test variables
        private const val locationName1 = "Hiano"
        private const val locationName2 = "feopikjfespokf"
        private const val locationName3 = "fexcvxcpokf"
    }

    private lateinit var databaseHandler: DatabaseConnector
    private lateinit var emailHandler: EmailHandler

    @Before
    fun setUp() {
        // TODO verify those emailhandler thignys
        emailHandler = mockk(relaxed = true)
        val tokenCaptureSlot1 = slot<String>()
        val tokenCaptureSlot2 = slot<String>()

        every { emailHandler.sendActivateAccount(email1, capture(tokenCaptureSlot1)) } answers {
            resetToken1 = tokenCaptureSlot1.captured
        }
        every { emailHandler.sendActivateAccount(email2, capture(tokenCaptureSlot2)) } answers {
            resetToken2 = tokenCaptureSlot2.captured
        }

        databaseHandler =
            DatabaseConnector(
                configuration,
                JdbcTemplate(dataSourceParam),
                NamedParameterJdbcTemplate(dataSourceParam),
                emailHandler
            )
        databaseHandler.dropAllTables()
        databaseHandler.createInitialTables()
        addTestUsers()
        addTestArticles()
        addTestLocations()
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
        // TODO improve this test
        databaseHandler.addArticle(name1, image1, description1)
        databaseHandler.addArticle(name2, image2, description2)

//        Assert.assertThat(databaseHandler.addArticle(name1, image1, shelf1), `is`(1))
//        Assert.assertThat(databaseHandler.addArticle(name2, image2, shelf2), `is`(2))
    }

    @Test
    fun testGetArticles() {

        val results = databaseHandler.getArticles(listOf(), listOf(), true)
        Assert.assertThat(results.size, `is`(2))
        val firstResult = results[0]
        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(name1))
        Assert.assertThat(firstResult.image, `is`(image1))
        Assert.assertThat(firstResult.description, `is`(description1))

        val secondResult = results[1]
        Assert.assertThat(secondResult.id, `is`(2))
        Assert.assertThat(secondResult.name, `is`(name2))
        Assert.assertThat(secondResult.image, `is`(image2))
        Assert.assertThat(secondResult.description, `is`(description2))
    }

    @Test
    fun testAddLocation_GetLocation() {
        var result = databaseHandler.getLocations(listOf())
        Assert.assertThat(result.size, `is`(3))

        result = databaseHandler.getLocations(listOf(1, 2, 3))
        Assert.assertThat(result.size, `is`(3))

        result = databaseHandler.getLocations(listOf(3, 4, 5))
        Assert.assertThat(result.size, `is`(1))

        result = databaseHandler.getLocations(listOf(345, 4356))
        Assert.assertThat(result.size, `is`(0))

        result = databaseHandler.getLocations(listOf(1, 3))
        Assert.assertThat(result.size, `is`(2))
    }

    @Test
    fun testUpdateArticle() {
        // Confirm the current values
        var results = databaseHandler.getArticles(listOf(1), listOf(), true)
        Assert.assertThat(results.size, `is`(1))
        var firstResult = results[0]
        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(name1))
        Assert.assertThat(firstResult.image, `is`(image1))
        Assert.assertThat(firstResult.description, `is`(description1))

        // Add new values
        val newName = "mooawfewefe"
        val newImage = "fsdmpofsd.png"
        val newDescription = "descriptionwef"
        databaseHandler.updateArticle(1, newName, newImage, newDescription)

        // Check that new values are active
        results = databaseHandler.getArticles(listOf(1), listOf(), true)
        Assert.assertThat(results.size, `is`(1))
        firstResult = results[0]
        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(newName))
        Assert.assertThat(firstResult.image, `is`(newImage))
        Assert.assertThat(firstResult.description, `is`(newDescription))
    }

    @Test
    fun testUpdateLocation() {
        // Confirm the current values
        var result = databaseHandler.getLocations(listOf(1))
        Assert.assertThat(result.size, `is`(1))
        var firstResult = result[0]

        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(locationName1))

        // Add new values
        val newName = "mooawdasfewefe"
        databaseHandler.updateLocation(1, newName)

        // check new values
        result = databaseHandler.getLocations(listOf(1))
        Assert.assertThat(result.size, `is`(1))
        firstResult = result[0]

        Assert.assertThat(firstResult.id, `is`(1))
        Assert.assertThat(firstResult.name, `is`(newName))
    }

    @Test
    fun testUpdateUser() {
        var results = databaseHandler.getUsers(listOf(1))

        // Confirm the current values
        Assert.assertThat(results.size, `is`(1))
        var firstResult = results[0] as UserResult
        Assert.assertThat(firstResult.firstName, `is`(firstName1))
        Assert.assertThat(firstResult.lastName, `is`(lastName1))
        Assert.assertThat(firstResult.email, `is`(email1))
        Assert.assertThat(firstResult.admin, `is`(admin1))

        // Add new values
        val newFirstName = "mooawfewefe"
        val newLastName = "fsdmklmdsklm"
        val newEmail = "fsdmpofsdvsd"
        val newAdmin = false
        databaseHandler.updateUser(1, newFirstName, newLastName, newEmail, newAdmin)

        // Confirm new values
        results = databaseHandler.getUsers(listOf(1))
        Assert.assertThat(results.size, `is`(1))
        firstResult = results[0] as UserResult
        Assert.assertThat(firstResult.firstName, `is`(newFirstName))
        Assert.assertThat(firstResult.lastName, `is`(newLastName))
        Assert.assertThat(firstResult.email, `is`(newEmail))
        Assert.assertThat(firstResult.admin, `is`(newAdmin))
    }

    @Test
    fun testSetArticlesAtLocation() {

        databaseHandler.setArticlesAtLocation(1, 1, 5)
        databaseHandler.setArticlesAtLocation(2, 2, 4)
        databaseHandler.setArticlesAtLocation(1, 2, 1)

        var result = databaseHandler.getArticles(listOf(), listOf())
        Assert.assertThat(result.size, `is`(2))
        Assert.assertThat(result[0].locations!!.size, `is`(1))
        Assert.assertThat(result[1].locations!!.size, `is`(2))

        result = databaseHandler.getArticles(listOf(1), listOf())
        Assert.assertThat(result.size, `is`(1))
        Assert.assertThat(result[0].locations!!.size, `is`(1))

        result = databaseHandler.getArticles(listOf(), listOf(1))
        Assert.assertThat(result.size, `is`(2))
        Assert.assertThat(result[0].locations!!.size, `is`(1))
        Assert.assertThat(result[1].locations!!.size, `is`(1))
    }

    private fun addTestUsers() {
        databaseHandler.addUser(firstName1, lastName1, email1, admin1)
        databaseHandler.setPassword(resetToken1, password1)
        databaseHandler.addUser(firstName2, lastName2, email2, admin2)
        databaseHandler.setPassword(resetToken2, password2)
    }

    private fun addTestArticles() {
        databaseHandler.addArticle(name1, image1, description1)
        databaseHandler.addArticle(name2, image2, description2)
    }

    private fun addTestLocations() {
        databaseHandler.addLocation(locationName1)
        databaseHandler.addLocation(locationName2)
        databaseHandler.addLocation(locationName3)
    }
}
