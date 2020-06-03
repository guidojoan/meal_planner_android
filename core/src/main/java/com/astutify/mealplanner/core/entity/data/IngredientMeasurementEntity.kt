package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientMeasurementEntity(
    val id: String,
    val quantity: Float,
    val measurementId: String
)
