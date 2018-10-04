package com.fcgtalent.fcgcatalog.database

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class DatabaseConnectorTest {

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
        val name = "Moo1"
        val password = "hiano"
        val hashedPass = "x12356"
        val email = "joo@koo.com"

        mockkStatic(BCrypt::class)
        every { BCrypt.hashpw(any(), any()) } returns hashedPass

        databaseConnector.addUser(name, password, email)

        verify(exactly = 1) {
            databaseConnection.prepareStatement(any())
            preparedStatement.setString(1, name)
            preparedStatement.setString(2, hashedPass)
            preparedStatement.setString(3, email)
            preparedStatement.executeUpdate()
            BCrypt.hashpw(password, any())
        }
    }

    @Test
    fun testCreateInitialTables() {

        val createTable = "CREATE TABLE"
        val statement: Statement = mockk()
        every { databaseConnection.createStatement() } returns statement
        every { statement.execute(createTable) } returns true

        // Recreate, so we can call protected function that we are testing
        databaseConnector = object: DatabaseConnector(configuration) {
            override val connection : Connection
                get() = databaseConnection
            init {
                createInitialTables()
            }
        }

        verify(exactly = 4) { statement.execute(any()) }
    }
}