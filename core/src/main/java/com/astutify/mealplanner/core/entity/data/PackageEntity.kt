package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PackageEntity(
    val id: String,
    val name: String,
    val quantity: Float
)
