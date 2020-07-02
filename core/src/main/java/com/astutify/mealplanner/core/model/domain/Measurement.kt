package com.astutify.mealplanner.core.model.domain

data class Measurement(
    val id: String,
    val primary: Boolean,
    val localizations: List<MeasurementLocalization>
)
