package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.UnitTests
import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category
import org.springframework.security.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.PreparedStatement

@Category(UnitTests::class)
class DatabaseConnectorTest_old {

    private lateinit var databaseConnector: DatabaseConnector
    private lateinit var databaseConnection: Connection
    private lateinit var configuration: DatabaseConfiguration
    private lateinit var preparedStatement: PreparedStatement

    @Before
    fun setUp() {
        databaseConnection = mockk()
        configuration = mockk()
        preparedStatement = mockk(relaxed = true)

        every { databaseConnection.prepareStatement(any()) } returns preparedStatement

        databaseConnector = object : DatabaseConnector(configuration) {
            override val connection: Connection
                get() = databaseConnection
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testAddUser() {
        val firstName = "Moo1"
        val lastName = "MOoo2"
        val password = "hiano"
        val hashedPass = "x12356"
        val email = "joo@koo.com"
        val admin = true

        mockkStatic(BCrypt::class)
        every { BCrypt.hashpw(any(), any()) } returns hashedPass

        databaseConnector.addUser(firstName, lastName, password, email, admin)

        verify(exactly = 1) {
            databaseConnection.prepareStatement(any())
            preparedStatement.setString(1, firstName)
            preparedStatement.setString(2, lastName)
            preparedStatement.setString(3, hashedPass)
            preparedStatement.setString(4, email)
            preparedStatement.setInt(5, if (admin) 1 else 0)
            preparedStatement.executeUpdate()
            BCrypt.hashpw(password, any())
        }
    }


    // TODO fix this
//    @Test
//    fun testCreateInitialTables() {
//
//        val createTable = "CREATE TABLE"
//        val statement: Statement = mockk()
//        every { databaseConnection.createStatement() } returns statement
//        every { statement.execute(createTable) } returns true
//        every { configuration.type } returns "sqlite"
//
//        // Recreate, so we can call protected function that we are testing
//        databaseConnector = object : DatabaseConnector(configuration) {
//            override val connection: Connection
//                get() = databaseConnection
//            init {
//                createInitialTables()
//            }
//        }
//
//        verify(exactly = 4) { statement.execute(any()) }
//    }
}