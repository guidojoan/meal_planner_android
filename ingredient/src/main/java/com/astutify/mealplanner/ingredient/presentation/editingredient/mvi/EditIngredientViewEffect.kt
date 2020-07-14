package com.astutify.mealplanner.ingredient.presentation.editingredient.mvi

sealed class EditIngredientViewEffect {
    object LoadData : EditIngredientViewEffect()
    object Save : EditIngredientViewEffect()
    object GoBack : EditIngredientViewEffect()
}
