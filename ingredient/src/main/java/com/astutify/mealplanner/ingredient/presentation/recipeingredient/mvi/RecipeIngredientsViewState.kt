package com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeIngredientsViewState(
    val ingredients: List<IngredientViewModel> = emptyList(),
    val loading: Loading? = null,
    val error: Error? = null,
    val noResults: Boolean = false,
    val selectedIngredient: IngredientViewModel? = null
) : Parcelable {
    fun copyState(
        ingredients: List<IngredientViewModel> = this.ingredients,
        loading: Loading? = null,
        error: Error? = null,
        noResults: Boolean = this.noResults,
        selectedIngredient: IngredientViewModel? = this.selectedIngredient
    ) = copy(
        ingredients = ingredients,
        loading = loading,
        error = error,
        noResults = noResults,
        selectedIngredient = selectedIngredient
    )

    enum class Loading {
        LOADING, LOADING_NEXT
    }

    enum class Error {
        LOADING_ERROR, LOADING_NEXT_ERROR
    }
}