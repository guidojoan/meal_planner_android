package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientGroupApi(
    val id: String,
    val name: String,
    val recipeIngredients: List<RecipeIngredientApi>
)
