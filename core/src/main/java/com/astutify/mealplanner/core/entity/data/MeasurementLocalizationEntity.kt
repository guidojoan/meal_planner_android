package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasurementLocalizationEntity(
    val name: String,
    val imperialSuffix: String,
    val metricSuffix: String,
    val imperial1000Suffix: String?,
    val metric1000Suffix: String?,
    val imperialSuffixPlural: String?,
    val metricSuffixPlural: String?,
    val imperial1000SuffixPlural: String?,
    val metric1000SuffixPlural: String?
)
