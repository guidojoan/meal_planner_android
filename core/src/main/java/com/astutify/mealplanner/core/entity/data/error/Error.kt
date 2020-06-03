package com.astutify.mealplanner.core.entity.data.error

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorBody(
    val message: String?,
    val errors: List<ErrorSpec>?
)

data class ErrorSpec(
    val path: String?,
    val validatorKey: String?,
    val message: String?
)
