package com.astutify.mealplanner.core.entity.domain

data class RecipeIngredient(
    val id: String,
    val quantity: Float,
    val ingredient: Ingredient,
    val measurement: Measurement
)
