package com.astutify.mealplanner.recipe.presentation.list.mvi

import com.astutify.mealplanner.coreui.model.RecipeViewModel

sealed class RecipesViewEffect {
    object CheckLoginStatus : RecipesViewEffect()
    object LoadData : RecipesViewEffect()
    object GoToAddRecipe : RecipesViewEffect()
    class GoToEditRecipe(val recipe: RecipeViewModel) : RecipesViewEffect()
    class GoToRecipeDetail(val recipe: RecipeViewModel) : RecipesViewEffect()
    class Search(val name: String) : RecipesViewEffect()
    object EndOfListReached : RecipesViewEffect()
}
