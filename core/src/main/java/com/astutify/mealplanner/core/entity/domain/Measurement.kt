package com.astutify.mealplanner.core.entity.domain

data class Measurement(
    val id: String,
    val primary: Boolean,
    val localizations: List<MeasurementLocalization>
)
