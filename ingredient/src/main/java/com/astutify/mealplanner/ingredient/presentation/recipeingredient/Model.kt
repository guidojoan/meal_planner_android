package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
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

sealed class RecipeIngredientsViewEvent {
    class Search(val name: String) : RecipeIngredientsViewEvent()
    object Loading : RecipeIngredientsViewEvent()
    object LoadingNext : RecipeIngredientsViewEvent()
    object LoadingError : RecipeIngredientsViewEvent()
    object LoadingNextError : RecipeIngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : RecipeIngredientsViewEvent()
    class NextDataLoaded(val ingredients: List<IngredientViewModel>) : RecipeIngredientsViewEvent()
    class IngredientClick(val ingredient: IngredientViewModel) : RecipeIngredientsViewEvent()
    class IngredientQuantitySet(val quantity: Float, val measurement: MeasurementViewModel) :
        RecipeIngredientsViewEvent()

    object ClickBack : RecipeIngredientsViewEvent()
    object ClickAddIngredient : RecipeIngredientsViewEvent()
    object EndOfListReached : RecipeIngredientsViewEvent()
}

sealed class RecipeIngredientsViewEffect {
    class Search(val name: String) : RecipeIngredientsViewEffect()
    class IngredientQuantitySet(
        val ingredient: IngredientViewModel,
        val quantity: Float,
        val measurement: MeasurementViewModel
    ) : RecipeIngredientsViewEffect()

    object ClickBack : RecipeIngredientsViewEffect()
    object GoToAddIngredient : RecipeIngredientsViewEffect()
    object LoadMoreData : RecipeIngredientsViewEffect()
}
