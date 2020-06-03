package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserEntity(
    val userId: String,
    val email: String?,
    val name: String
)
