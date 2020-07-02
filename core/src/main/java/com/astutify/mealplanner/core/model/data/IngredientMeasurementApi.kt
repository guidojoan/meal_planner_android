package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientMeasurementApi(
    val id: String,
    val quantity: Float,
    val measurementId: String
)
