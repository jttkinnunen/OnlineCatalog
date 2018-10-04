package com.fcgtalent.fcgcatalog

import com.fcgtalent.fcgcatalog.configuration.DatabaseConfiguration
import com.fcgtalent.fcgcatalog.database.DatabaseHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FCGCatalogApplication

fun main(args: Array<String>) {
    val context = runApplication<FCGCatalogApplication>(*args)
    val config = context.getBean(DatabaseConfiguration::class.java)
    val databaseHandler = context.getBean(DatabaseHandler::class.java, config)
}
