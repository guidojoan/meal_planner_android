package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthData(
    val token: String,
    val refreshToken: String,
    val houseId: String? = null
)
