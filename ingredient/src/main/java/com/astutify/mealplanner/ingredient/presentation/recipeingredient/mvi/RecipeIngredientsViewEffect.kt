package com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi

import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel

sealed class RecipeIngredientsViewEffect {
    class Search(val name: String) : RecipeIngredientsViewEffect()
    class IngredientQuantitySet(
        val ingredient: IngredientViewModel,
        val quantity: Float,
        val measurement: MeasurementViewModel
    ) : RecipeIngredientsViewEffect()

    object ClickBack : RecipeIngredientsViewEffect()
    object GoToAddIngredient : RecipeIngredientsViewEffect()
    object LoadMoreData : RecipeIngredientsViewEffect()
}
