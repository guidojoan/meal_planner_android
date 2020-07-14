package com.astutify.mealplanner.ingredient.presentation.list.mvi

import com.astutify.mealplanner.coreui.model.IngredientViewModel

sealed class IngredientsViewEvent {
    object Loading : IngredientsViewEvent()
    object LoadData : IngredientsViewEvent()
    object LoadingNext : IngredientsViewEvent()
    object LoadingError : IngredientsViewEvent()
    object LoadingNextError : IngredientsViewEvent()
    class Search(val name: String) : IngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
    class NextDataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
    class IngredientClicked(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickAddIngredient : IngredientsViewEvent()
    class IngredientAdded(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    class IngredientUpdated(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickRefresh : IngredientsViewEvent()
    object ClickCloseSearch : IngredientsViewEvent()
    object EndOfListReached : IngredientsViewEvent()
}