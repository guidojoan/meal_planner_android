package com.astutify.mealplanner.recipe.presentation.detail

import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.mvi.Effect
import com.astutify.mealplanner.coreui.presentation.mvi.Reducer
import com.astutify.mealplanner.coreui.presentation.mvi.ReducerResult
import com.astutify.mealplanner.coreui.presentation.mvi.State
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
