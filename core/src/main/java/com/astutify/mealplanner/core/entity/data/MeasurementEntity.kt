package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasurementEntity(
    val id: String,
    val primary: Boolean,
    val localizations: List<MeasurementLocalizationEntity>
)
