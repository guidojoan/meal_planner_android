package com.astutify.mealplanner.recipe.presentation.list

import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import io.reactivex.Observable

interface RecipesView {

    fun render(viewState: RecipesViewState)

    val events: Observable<Intent>

    sealed class Intent {
        object ClickAddRecipe : Intent()
        object ClickRetry : Intent()
        object ClickRefresh : Intent()
        object SearchCancelClicked : Intent()
        class Search(val name: String) : Intent()
        class RecipeClicked(val recipe: RecipeViewModel) : Intent()
        class RecipeLongClicked(val recipe: RecipeViewModel) : Intent()
        class RecipeAdded(val recipe: RecipeViewModel) : Intent()
        class RecipeUpdated(val recipe: RecipeViewModel) : Intent()
        class RecipeDeleted(val recipe: RecipeViewModel) : Intent()
        object EndOfListReached : Intent()
    }
}
