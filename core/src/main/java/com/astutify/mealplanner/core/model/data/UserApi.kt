package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserApi(
    val userId: String,
    val email: String?,
    val name: String
)
