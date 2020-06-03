package com.astutify.mealplanner.core.entity.domain

data class IngredientGroup(
    val id: String,
    val name: String,
    val recipeIngredients: List<RecipeIngredient>
)
