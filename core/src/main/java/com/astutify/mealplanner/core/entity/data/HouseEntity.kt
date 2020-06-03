package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HouseEntity(
    val id: String,
    val name: String,
    val joinCode: Int
)
