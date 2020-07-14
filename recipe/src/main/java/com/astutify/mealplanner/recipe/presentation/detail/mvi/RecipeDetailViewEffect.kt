package com.astutify.mealplanner.recipe.presentation.detail.mvi

sealed class RecipeDetailViewEffect {
    object GoBack : RecipeDetailViewEffect()
}
