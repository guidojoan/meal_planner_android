package com.astutify.mealplanner.recipe.presentation.edit.mvi

import com.astutify.mealplanner.coreui.model.RecipeViewModel

sealed class EditRecipeViewEffect {
    class SaveRecipe(val recipe: RecipeViewModel) : EditRecipeViewEffect()
    object LoadData : EditRecipeViewEffect()
    class GoToRecipeIngredients(val ingredientGroupId: String) : EditRecipeViewEffect()
    object GoBack : EditRecipeViewEffect()
    object Delete : EditRecipeViewEffect()
}
