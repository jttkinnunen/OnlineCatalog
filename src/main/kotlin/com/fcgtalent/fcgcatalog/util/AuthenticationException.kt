package com.fcgtalent.fcgcatalog.util

import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthenticationException constructor(message: String = "Authentication Error") : Exception(message) {
    fun toResponseEntity(): ResponseEntity<String> {
        val errorJSON = JSONObject()
        errorJSON.put("error", message)
        return ResponseEntity(errorJSON.toString(), HttpStatus.FORBIDDEN)
    }
}