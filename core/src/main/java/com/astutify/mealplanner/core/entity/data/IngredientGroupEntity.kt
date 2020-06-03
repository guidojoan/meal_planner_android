package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientGroupEntity(
    val id: String,
    val name: String,
    val recipeIngredients: List<RecipeIngredientEntity>
)
