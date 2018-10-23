package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseConnector
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class FCGCatalogApplicationTests {

    @Autowired
    lateinit var context: ApplicationContext
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Before
    fun setUp() {
        val config = context.getBean(DatabaseConfiguration::class.java)
        val databaseConnector = context.getBean(DatabaseConnector::class.java, config)
    }

    @Test
    fun testWorkingTest() {
//        Assert.assertTrue(true)
//        val sqliteConfiguration: DatabaseConfiguration = mockk()
//        every { sqliteConfiguration.sqlitememory } returns true
//        every { sqliteConfiguration.type } returns "sqlite"
//
//        val databaseConnector = context.getBean(DatabaseConnector::class.java, sqliteConfiguration)
//        databaseConnector.createInitialTables()
//        databaseConnector.addUser("moo1", "moo2", "moo3", "moo4", true)
//
//        Assert.assertThat(databaseConnector.getUsers(listOf()).size, `is`(0))
//        Assert.assertThat(databaseConnector.getUsers(listOf(1)).size, `is`(1))
//        Assert.assertThat(databaseConnector.getUsers(listOf(2)).size, `is`(0))
//
//        databaseConnector.getUserWithToken("momoo")
//
//        databaseConnector.addArticle("moo1", "moo2", "moo3")
//
//        Assert.assertThat(databaseConnector.getArticles(listOf()).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticles(listOf(1)).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticles(listOf(2)).size, `is`(0))
//
//        databaseConnector.addLocation("moo1")
//
//        Assert.assertThat(databaseConnector.getLocations(listOf()).size, `is`(1))
//        Assert.assertThat(databaseConnector.getLocations(listOf(1)).size, `is`(1))
//        Assert.assertThat(databaseConnector.getLocations(listOf(2)).size, `is`(0))
//        databaseConnector.addUser("moo1", "moo2", "passu1", "user1", true)
//
//        val result = databaseConnector.login("user1", "passu1")
//        Assert.assertNotNull(result.token, result.token)
//
//        databaseConnector.logout(result.token!!)
//
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(), listOf()).size, `is`(0))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(1), listOf()).size, `is`(0))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(), listOf(1)).size, `is`(0))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(1), listOf(1)).size, `is`(0))
//
//        databaseConnector.setArticlesAtLocation(1, 1, 5)
//
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(), listOf()).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(1), listOf()).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(), listOf(1)).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(1), listOf(1)).size, `is`(1))
//        Assert.assertThat(databaseConnector.getArticlesInLocations(listOf(1), listOf(2)).size, `is`(0))
    }
}