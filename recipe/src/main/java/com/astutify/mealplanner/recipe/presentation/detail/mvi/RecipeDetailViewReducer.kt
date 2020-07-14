package com.astutify.mealplanner.recipe.presentation.detail.mvi

import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class RecipeDetailViewReducer @Inject constructor(
    val recipe: RecipeViewModel
) :
    Reducer<RecipeDetailViewState, RecipeDetailViewEvent, RecipeDetailViewEffect>() {

    override fun invoke(
        state: RecipeDetailViewState,
        event: RecipeDetailViewEvent
    ): ReducerResult<RecipeDetailViewState, RecipeDetailViewEffect> {
        return when (event) {
            RecipeDetailViewEvent.ClickBack -> {
                Effect(RecipeDetailViewEffect.GoBack)
            }
            is RecipeDetailViewEvent.ServingsChanged -> {
                State(state.copy(recipe = recipe.copyWithNewServings(event.servings)))
            }
        }
    }
}
