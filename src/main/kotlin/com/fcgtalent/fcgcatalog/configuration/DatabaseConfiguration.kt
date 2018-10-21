package com.fcgtalent.fcgcatalog.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "database")
class DatabaseConfiguration() {

    lateinit var type: String
    var address: String? = null
    lateinit var name: String
    var username: String? = null
    var password: String? = null
    var port: String? = null

    var sqlitememory: Boolean = false


}

