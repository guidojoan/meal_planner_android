package com.astutify.mealplanner.ingredient.presentation.list.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientsViewState(
    val ingredients: List<IngredientViewModel> = emptyList(),
    val loading: Loading? = null,
    val error: Error? = null
) : Parcelable {
    fun copyState(
        ingredients: List<IngredientViewModel> = this.ingredients,
        loading: Loading? = null,
        error: Error? = null
    ) = copy(ingredients = ingredients, loading = loading, error = error)

    enum class Loading {
        LOADING, LOADING_NEXT
    }

    enum class Error {
        LOADING_ERROR, NO_RESULTS, LOADING_NEXT_ERROR
    }
}