package com.astutify.mealplanner.core.model.domain

data class IngredientGroup(
    val id: String,
    val name: String,
    val recipeIngredients: List<RecipeIngredient>
)
