package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeApi(
    val id: String,
    val name: String,
    val directions: String,
    val servings: Int,
    val ingredientGroups: List<IngredientGroupApi>,
    val imageUrl: String,
    val categoryId: String
)
