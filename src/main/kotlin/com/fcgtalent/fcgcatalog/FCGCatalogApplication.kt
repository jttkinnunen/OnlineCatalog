package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.mail.javamail.JavaMailSender
import javax.sql.DataSource
import org.springframework.mail.SimpleMailMessage



@SpringBootApplication
class FCGCatalogApplication {

    @Bean
    fun dataSource(databaseConfiguration: DatabaseConfiguration): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        when (databaseConfiguration.type) {
            "sqlite" -> {
                dataSourceBuilder.driverClassName("org.sqlite.JDBC")
                if (databaseConfiguration.sqlitememory) dataSourceBuilder.url("jdbc:sqlite::memory:")
                else dataSourceBuilder.url("jdbc:sqlite:${databaseConfiguration.name}.db")
            }
            "pgsql" -> {
                dataSourceBuilder.driverClassName("org.postgresql.Driver")
                dataSourceBuilder.url("jdbc:postgresql://${databaseConfiguration.address}:${databaseConfiguration.port}/${databaseConfiguration.name}")
                dataSourceBuilder.username(databaseConfiguration.username)
                dataSourceBuilder.password(databaseConfiguration.password)
            }
            else -> throw Exception("Unsupported database type")
        }
        return dataSourceBuilder.build()
    }
}

fun main(args: Array<String>) {
    val context = runApplication<FCGCatalogApplication>(*args)
    val config = context.getBean(DatabaseConfiguration::class.java)
    val roskaMoska = context.getBean(JdbcTemplate::class.java)
}
