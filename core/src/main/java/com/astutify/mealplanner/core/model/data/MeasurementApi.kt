package com.astutify.mealplanner.core.model.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasurementApi(
    val id: String,
    val primary: Boolean,
    val localizations: List<MeasurementLocalizationApi>
)
