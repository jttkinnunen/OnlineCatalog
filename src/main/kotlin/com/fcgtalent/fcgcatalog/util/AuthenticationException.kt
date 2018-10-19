package com.fcgtalent.fcgcatalog.util

import com.fcgtalent.fcgcatalog.CatalogController
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthenticationException constructor(message: String = "Authentication Error") : Exception(message) {
    fun toResponseEntity(): ResponseEntity<CatalogController.OurError> {
        return ResponseEntity(CatalogController.OurError(message!!), HttpStatus.FORBIDDEN)
    }
}