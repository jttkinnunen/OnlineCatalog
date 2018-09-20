package com.fcgtalent.fcgcatalog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FCGCatalogApplication

fun main(args: Array<String>) {
    SpringApplication.run(FCGCatalogApplication::class.java, *args)
}
