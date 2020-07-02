package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeIngredientApi(
    val id: String,
    val quantity: Float,
    val ingredient: IngredientApi,
    val measurementId: String
)
