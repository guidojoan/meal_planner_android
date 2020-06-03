package com.astutify.mealplanner.core.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageEntity<T>(
    val results: List<T>,
    val paging: PagingEntity
)

@JsonClass(generateAdapter = true)
data class PagingEntity(
    val next: Int?
)
