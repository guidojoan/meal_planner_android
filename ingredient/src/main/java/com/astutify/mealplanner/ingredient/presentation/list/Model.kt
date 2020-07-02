package com.astutify.mealplanner.ingredient.presentation.list

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

sealed class IngredientsViewEvent {
    object Loading : IngredientsViewEvent()
    object LoadData : IngredientsViewEvent()
    object LoadingNext : IngredientsViewEvent()
    object LoadingError : IngredientsViewEvent()
    object LoadingNextError : IngredientsViewEvent()
    class Search(val name: String) : IngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
    class NextDataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
    class IngredientClicked(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickAddIngredient : IngredientsViewEvent()
    class IngredientAdded(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    class IngredientUpdated(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickRefresh : IngredientsViewEvent()
    object ClickCloseSearch : IngredientsViewEvent()
    object EndOfListReached : IngredientsViewEvent()
}

sealed class IngredientsViewEffect {
    object LoadData : IngredientsViewEffect()
    class Search(val name: String) : IngredientsViewEffect()
    object GoToAddIngredient : IngredientsViewEffect()
    class GoToEditIngredient(val ingredient: IngredientViewModel) : IngredientsViewEffect()
    object LoadMoreData : IngredientsViewEffect()
}
