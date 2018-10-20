package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@SpringBootApplication
class FCGCatalogApplication

fun main(args: Array<String>) {
    val context = runApplication<FCGCatalogApplication>(*args)
    val config = context.getBean(DatabaseConfiguration::class.java)
    val databaseHandler = context.getBean(DatabaseHandler::class.java, config)
}

@Bean
fun dataSource(): DataSource {
    val dataSourceBuilder = DataSourceBuilder.create()
    dataSourceBuilder.driverClassName("org.sqlite.JDBC")
    dataSourceBuilder.url("jdbc:sqlite::memory:")
    return dataSourceBuilder.build()
}

@Bean
fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
    return JdbcTemplate(dataSource)
}