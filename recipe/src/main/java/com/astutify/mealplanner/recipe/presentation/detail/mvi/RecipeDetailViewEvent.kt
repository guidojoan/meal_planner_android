package com.astutify.mealplanner.recipe.presentation.detail.mvi

sealed class RecipeDetailViewEvent {
    object ClickBack : RecipeDetailViewEvent()
    data class ServingsChanged(val servings: Int) : RecipeDetailViewEvent()
}