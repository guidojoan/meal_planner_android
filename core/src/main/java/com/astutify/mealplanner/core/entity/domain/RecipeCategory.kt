package com.astutify.mealplanner.core.entity.domain

data class RecipeCategory(
    val id: String,
    val name: String
) {
    override fun toString() = name
}
