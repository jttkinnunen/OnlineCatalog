package com.fcgtalent.fcgcatalog.util

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Date
// TODO Comment and clean

// Answers we send back
data class UserResult @JsonCreator constructor(
        @JsonProperty val id: Int,
        @JsonProperty("first_name") val firstName: String,
        @JsonProperty("last_name") val lastName: String,
        @JsonProperty val email: String,
        @JsonProperty val admin: Boolean,
        @JsonProperty val token: String? = null
)

data class ArticleResult @JsonCreator constructor(
        @JsonProperty val id: Int, val name: String,
        @JsonProperty val brand: String?,
        @JsonProperty val quantity: Int,
        @JsonProperty val last_change: Date,
        @JsonProperty val shelf: String
)

// Received message bodies
data class AddUserBody @JsonCreator constructor(
        @JsonProperty("first_name") val firstName: String,
        @JsonProperty("last_name") val lastName: String,
        @JsonProperty val password: String,
        @JsonProperty val email: String,
        @JsonProperty val admin: Boolean,
        @JsonProperty val token: String?
)

data class GetUsersBody @JsonCreator constructor(@JsonProperty val token: String)

data class AddArticleBody @JsonCreator constructor(
        @JsonProperty val name: String,
        @JsonProperty val brand: String?,
        @JsonProperty val quantity: Int? = 0,
        @JsonProperty val shelf: String,
        @JsonProperty val token: String
)

data class GetArticlesBody @JsonCreator constructor(@JsonProperty val token: String)
data class LogoutBody  @JsonCreator constructor(@JsonProperty val token: String)

data class LoginBody @JsonCreator constructor(
        @JsonProperty val username: String,
        @JsonProperty val password: String
)
