package com.fcgtalent.fcgcatalog.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

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