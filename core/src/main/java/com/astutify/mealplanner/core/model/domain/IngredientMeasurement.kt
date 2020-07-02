package com.astutify.mealplanner.core.model.domain

data class IngredientMeasurement(
    val id: String,
    val quantity: Float,
    val measurement: Measurement
)
