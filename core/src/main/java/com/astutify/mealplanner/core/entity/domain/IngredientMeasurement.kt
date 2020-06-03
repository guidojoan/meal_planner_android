package com.astutify.mealplanner.core.entity.domain

data class IngredientMeasurement(
    val id: String,
    val quantity: Float,
    val measurement: Measurement
)
