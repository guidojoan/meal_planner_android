package com.astutify.mealplanner.core.model.domain

data class Recipe(
    val id: String,
    val name: String,
    val directions: String,
    val servings: Int,
    val ingredientGroups: List<IngredientGroup>,
    val imageUrl: String,
    val category: RecipeCategory
)
