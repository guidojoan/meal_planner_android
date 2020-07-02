package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AboutApi(
    val about: String,
    val backendVersion: String
)
