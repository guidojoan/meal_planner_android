package com.astutify.mealplanner.recipe.presentation.list.mvi

import com.astutify.mealplanner.coreui.model.RecipeViewModel

sealed class RecipesViewEvent {
    object CheckLoginStatus : RecipesViewEvent()
    object Load : RecipesViewEvent()
    object LoadingError : RecipesViewEvent()
    object LoadingNextError : RecipesViewEvent()
    object Loading : RecipesViewEvent()
    object LoadingNext : RecipesViewEvent()
    object NetworkError : RecipesViewEvent()
    class DataLoaded(val recipes: List<RecipeViewModel>) : RecipesViewEvent()
    class NextDataLoaded(val recipes: List<RecipeViewModel>) : RecipesViewEvent()
    object ClickAddRecipe : RecipesViewEvent()
    class RecipeClicked(val recipe: RecipeViewModel) : RecipesViewEvent()
    class RecipeLongClicked(val recipe: RecipeViewModel) : RecipesViewEvent()
    class RecipeAdded(val recipe: RecipeViewModel) : RecipesViewEvent()
    class RecipeUpdated(val recipe: RecipeViewModel) : RecipesViewEvent()
    class RecipeDeleted(val recipe: RecipeViewModel) : RecipesViewEvent()
    object ClickRefresh : RecipesViewEvent()
    object ClickCloseSearch : RecipesViewEvent()
    class Search(val name: String) : RecipesViewEvent()
    object EndOfListReached : RecipesViewEvent()
}