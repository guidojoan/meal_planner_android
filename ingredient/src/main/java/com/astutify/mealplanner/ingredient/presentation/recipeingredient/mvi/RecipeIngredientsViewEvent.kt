package com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi

import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel

sealed class RecipeIngredientsViewEvent {
    class Search(val name: String) : RecipeIngredientsViewEvent()
    object Loading : RecipeIngredientsViewEvent()
    object LoadingNext : RecipeIngredientsViewEvent()
    object LoadingError : RecipeIngredientsViewEvent()
    object LoadingNextError : RecipeIngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : RecipeIngredientsViewEvent()
    class NextDataLoaded(val ingredients: List<IngredientViewModel>) : RecipeIngredientsViewEvent()
    class IngredientClick(val ingredient: IngredientViewModel) : RecipeIngredientsViewEvent()
    class IngredientQuantitySet(val quantity: Float, val measurement: MeasurementViewModel) :
        RecipeIngredientsViewEvent()

    object ClickBack : RecipeIngredientsViewEvent()
    object ClickAddIngredient : RecipeIngredientsViewEvent()
    object EndOfListReached : RecipeIngredientsViewEvent()
}