package com.astutify.mealplanner.ingredient.presentation.list.mvi

import com.astutify.mealplanner.coreui.model.IngredientViewModel

sealed class IngredientsViewEffect {
    object LoadData : IngredientsViewEffect()
    class Search(val name: String) : IngredientsViewEffect()
    object GoToAddIngredient : IngredientsViewEffect()
    class GoToEditIngredient(val ingredient: IngredientViewModel) : IngredientsViewEffect()
    object LoadMoreData : IngredientsViewEffect()
}
