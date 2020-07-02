package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PackageApi(
    val id: String,
    val name: String,
    val quantity: Float
)
