package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AboutEntity(
    val about: String,
    val backendVersion: String
)
