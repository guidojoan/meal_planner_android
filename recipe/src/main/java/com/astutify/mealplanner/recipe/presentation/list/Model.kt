package com.astutify.mealplanner.recipe.presentation.list

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipesViewState(
    val recipes: List<RecipeViewModel> = emptyList(),
    val loading: Loading? = null,
    val error: Error? = null
) : Parcelable {
    fun copyState(
        recipes: List<RecipeViewModel> = this.recipes,
        loading: Loading? = null,
        error: Error? = null
    ) = copy(recipes = recipes, loading = loading, error = error)

    enum class Loading {
        LOADING, LOADING_NEXT
    }

    enum class Error {
        LOADING_ERROR, NO_RESULTS, LOADING_NEXT_ERROR
    }
}

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

sealed class RecipesViewEffect {
    object CheckLoginStatus : RecipesViewEffect()
    object LoadData : RecipesViewEffect()
    object GoToAddRecipe : RecipesViewEffect()
    class GoToEditRecipe(val recipe: RecipeViewModel) : RecipesViewEffect()
    class GoToRecipeDetail(val recipe: RecipeViewModel) : RecipesViewEffect()
    class Search(val name: String) : RecipesViewEffect()
    object EndOfListReached : RecipesViewEffect()
}
