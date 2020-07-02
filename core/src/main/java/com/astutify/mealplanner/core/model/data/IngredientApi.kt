package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientApi(
    val id: String,
    val name: String,
    val measurements: List<IngredientMeasurementApi>,
    val categoryId: String,
    val packages: List<PackageApi>?
)
