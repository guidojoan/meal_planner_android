package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.presentation.mvi.Effect
import com.astutify.mealplanner.coreui.presentation.mvi.Next
import com.astutify.mealplanner.coreui.presentation.mvi.Reducer
import com.astutify.mealplanner.coreui.presentation.mvi.ReducerResult
import com.astutify.mealplanner.coreui.presentation.mvi.State
import javax.inject.Inject

class RecipeIngredientsViewReducer @Inject constructor() :
    Reducer<RecipeIngredientsViewState, RecipeIngredientsViewEvent, RecipeIngredientsViewEffect>() {

    override fun invoke(
        state: RecipeIngredientsViewState,
        event: RecipeIngredientsViewEvent
    ): ReducerResult<RecipeIngredientsViewState, RecipeIngredientsViewEffect> {
        return when (event) {
            is RecipeIngredientsViewEvent.Search -> {
                Next(
                    state = state.copyState(selectedIngredient = null),
                    effect = RecipeIngredientsViewEffect.Search(event.name)
                )
            }
            is RecipeIngredientsViewEvent.DataLoaded -> {
                State(
                    state = state.copyState(
                        ingredients = event.ingredients
                    )
                )
            }
            is RecipeIngredientsViewEvent.IngredientClick -> {
                State(
                    state = state.copyState(
                        selectedIngredient = event.ingredient
                    )
                )
            }
            is RecipeIngredientsViewEvent.IngredientQuantitySet -> {
                Effect(
                    effect = RecipeIngredientsViewEffect.IngredientQuantitySet(
                        state.selectedIngredient!!,
                        event.quantity,
                        event.measurement
                    )
                )
            }
            RecipeIngredientsViewEvent.ClickBack -> {
                Effect(RecipeIngredientsViewEffect.ClickBack)
            }
            RecipeIngredientsViewEvent.ClickAddIngredient -> {
                Effect(RecipeIngredientsViewEffect.GoToAddIngredient)
            }
            RecipeIngredientsViewEvent.Loading -> {
                State(state.copyState(loading = RecipeIngredientsViewState.Loading.LOADING))
            }
            RecipeIngredientsViewEvent.LoadingNext -> {
                State(state.copyState(loading = RecipeIngredientsViewState.Loading.LOADING_NEXT))
            }
            RecipeIngredientsViewEvent.LoadingError -> {
                State(state.copyState(error = RecipeIngredientsViewState.Error.LOADING_ERROR))
            }
            RecipeIngredientsViewEvent.LoadingNextError -> {
                State(state.copyState(error = RecipeIngredientsViewState.Error.LOADING_NEXT_ERROR))
            }
            is RecipeIngredientsViewEvent.NextDataLoaded -> {
                State(
                    state.copyState(
                        ingredients = state.ingredients.toMutableList().apply {
                            addAll(
                                event.ingredients
                            )
                        }
                    )
                )
            }
            RecipeIngredientsViewEvent.EndOfListReached -> {
                Effect(RecipeIngredientsViewEffect.LoadMoreData)
            }
        }
    }
}
