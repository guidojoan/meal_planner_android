package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientEntity(
    val id: String,
    val name: String,
    val measurements: List<IngredientMeasurementEntity>,
    val categoryId: String,
    val packages: List<PackageEntity>?
)
