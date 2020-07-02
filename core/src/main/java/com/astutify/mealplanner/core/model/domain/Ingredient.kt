package com.astutify.mealplanner.core.model.domain

data class Ingredient(
    val id: String,
    val name: String,
    val measurements: List<IngredientMeasurement>,
    val category: IngredientCategory,
    val packages: List<IngredientPackage>?
)
