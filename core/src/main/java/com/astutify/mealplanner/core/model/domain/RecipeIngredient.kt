package com.astutify.mealplanner.core.model.domain

data class RecipeIngredient(
    val id: String,
    val quantity: Float,
    val ingredient: Ingredient,
    val measurement: Measurement
)
