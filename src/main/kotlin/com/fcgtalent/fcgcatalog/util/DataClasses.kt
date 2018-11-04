package com.fcgtalent.fcgcatalog.util

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

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

/**
 *
 * article result is var, so it can be set to null if needed
 */
data class ArticleResult @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty val name: String,
    @JsonProperty val image: String?,
    @JsonProperty val last_change: Timestamp,
    @JsonProperty var description: String?,
    @JsonProperty val locations: MutableList<LocationQuantityResult>? = null
)

data class LocationResult @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty val name: String
)

data class LocationQuantityResult @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty val name: String,
    @JsonProperty val quantity: Int
)

// Received message bodies
data class AddUserBody @JsonCreator constructor(
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String,
    @JsonProperty val email: String,
    @JsonProperty val admin: Boolean,
    @JsonProperty val token: String
)

data class UpdateUserBody @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String,
    @JsonProperty val email: String,
    @JsonProperty val admin: Boolean,
    @JsonProperty val token: String
)

data class GetUsersBody @JsonCreator constructor(
    @JsonProperty val ids: List<Int>?,
    @JsonProperty val token: String
)

data class GetSelfBody @JsonCreator constructor(
    @JsonProperty val token: String
)

data class AddArticleBody @JsonCreator constructor(
    @JsonProperty val name: String,
    @JsonProperty val image: String?,
    @JsonProperty val description: String,
    @JsonProperty val token: String
)

data class UpdateArticleBody @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty val name: String,
    @JsonProperty val image: String?,
    @JsonProperty val description: String,
    @JsonProperty val token: String
)

data class AddLocationBody @JsonCreator constructor(
    @JsonProperty val name: String,
    @JsonProperty val token: String
)

data class UpdateLocationBody @JsonCreator constructor(
    @JsonProperty val id: Int,
    @JsonProperty val name: String,
    @JsonProperty val token: String
)

data class GetLocationsBody @JsonCreator constructor(
    @JsonProperty val ids: List<Int>?,
    @JsonProperty val token: String
)

data class GetArticlesBody @JsonCreator constructor(
    @JsonProperty val ids: List<Int>?,
    @JsonProperty val token: String
)

data class GetArticlesInLocationsBody @JsonCreator constructor(
    @JsonProperty val articleIds: List<Int>?,
    @JsonProperty val locationIds: List<Int>?,
    @JsonProperty val token: String
)

data class SetArticlesAtLocationBody @JsonCreator constructor(
    @JsonProperty("article_id") val articleId: Int,
    @JsonProperty("location_id") val locationId: Int,
    @JsonProperty val quantity: Int,
    @JsonProperty val token: String
)

data class LogoutBody @JsonCreator constructor(@JsonProperty val token: String)

data class LoginBody @JsonCreator constructor(
    @JsonProperty val username: String,
    @JsonProperty val password: String
)

data class SetPasswordBody @JsonCreator constructor(
    @JsonProperty("reset_token") val resetToken: String,
    @JsonProperty val newPassword: String
)

data class StartPasswordResetBody @JsonCreator constructor(
    @JsonProperty val email: String
)