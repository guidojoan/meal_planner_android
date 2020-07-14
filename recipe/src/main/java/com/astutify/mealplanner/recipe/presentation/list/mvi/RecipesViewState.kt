package com.astutify.mealplanner.recipe.presentation.list.mvi

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