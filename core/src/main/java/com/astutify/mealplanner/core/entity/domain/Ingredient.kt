package com.astutify.mealplanner.core.entity.domain

data class Ingredient(
    val id: String,
    val name: String,
    val measurements: List<IngredientMeasurement>,
    val category: IngredientCategory,
    val packages: List<IngredientPackage>?
)
