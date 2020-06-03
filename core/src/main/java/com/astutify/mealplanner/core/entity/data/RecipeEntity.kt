package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeEntity(
    val id: String,
    val name: String,
    val directions: String,
    val servings: Int,
    val ingredientGroups: List<IngredientGroupEntity>,
    val imageUrl: String,
    val categoryId: String
)
