package com.astutify.mealplanner.recipe.presentation.detail

import io.reactivex.Observable

interface RecipeDetailView {

    fun render(viewState: RecipeDetailViewState)

    val events: Observable<Intent>

    sealed class Intent {
        object ClickBack : Intent()
        data class ServingsChanged(val servings: Int) : Intent()
    }
}
