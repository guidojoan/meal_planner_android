package com.astutify.mealplanner.core.model.domain

data class MeasurementLocalization(
    val name: String,
    val imperialSuffix: String,
    val metricSuffix: String,
    val imperial1000Suffix: String? = null,
    val metric1000Suffix: String? = null,
    val imperialSuffixPlural: String? = null,
    val metricSuffixPlural: String? = null,
    val imperial1000SuffixPlural: String? = null,
    val metric1000SuffixPlural: String? = null
)
