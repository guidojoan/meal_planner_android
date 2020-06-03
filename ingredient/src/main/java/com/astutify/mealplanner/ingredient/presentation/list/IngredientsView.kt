package com.astutify.mealplanner.ingredient.presentation.list

import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import io.reactivex.Observable

interface IngredientsView {

    fun render(viewState: IngredientsViewState)

    val events: Observable<Intent>

    sealed class Intent {
        object ClickAddIngredient : Intent()
        object ClickRetry : Intent()
        data class Search(val name: String) : Intent()
        class IngredientClicked(val ingredient: IngredientViewModel) : Intent()
        class IngredientAdded(val ingredient: IngredientViewModel) : Intent()
        class IngredientUpdated(val ingredient: IngredientViewModel) : Intent()
        object ClickRefresh : Intent()
        object ClickCloseSearch : Intent()
        object OnEndOfListReached : Intent()
    }
}
