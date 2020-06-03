package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeIngredientEntity(
    val id: String,
    val quantity: Float,
    val ingredient: IngredientEntity,
    val measurementId: String
)
