package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@SpringBootApplication
class FCGCatalogApplication {

    @Bean
    fun dataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.sqlite.JDBC")
        dataSourceBuilder.url("jdbc:sqlite::memory:")
        return dataSourceBuilder.build()
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }
}

fun main(args: Array<String>) {
    val context = runApplication<FCGCatalogApplication>(*args)
    val config = context.getBean(DatabaseConfiguration::class.java)
    val roskaMoska = context.getBean(JdbcTemplate::class.java)
}


