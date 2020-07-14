package com.astutify.mealplanner.ingredient.presentation.list.mvi

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class IngredientsVewReducer @Inject constructor() :
    Reducer<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>() {

    override fun invoke(
        state: IngredientsViewState,
        event: IngredientsViewEvent
    ): ReducerResult<IngredientsViewState, IngredientsViewEffect> {
        return when (event) {
            is IngredientsViewEvent.LoadData -> Effect(
                IngredientsViewEffect.LoadData
            )
            is IngredientsViewEvent.Search -> Effect(
                IngredientsViewEffect.Search(
                    event.name
                )
            )
            is IngredientsViewEvent.Loading -> State(
                state.copyState(
                    loading = IngredientsViewState.Loading.LOADING
                )
            )
            is IngredientsViewEvent.NextDataLoaded -> State(
                state.copyState(ingredients = state.ingredients.toMutableList().apply { addAll(event.ingredients) })
            )
            is IngredientsViewEvent.DataLoaded -> State(
                state.copyState(
                    error = if (event.ingredients.isEmpty()) IngredientsViewState.Error.NO_RESULTS else null,
                    ingredients = event.ingredients
                )
            )
            is IngredientsViewEvent.LoadingError -> State(
                state.copyState(
                    error = IngredientsViewState.Error.LOADING_ERROR
                )
            )
            is IngredientsViewEvent.ClickAddIngredient -> Effect(
                IngredientsViewEffect.GoToAddIngredient
            )
            is IngredientsViewEvent.IngredientClicked -> {
                Effect(
                    IngredientsViewEffect.GoToEditIngredient(
                        event.ingredient
                    )
                )
            }
            is IngredientsViewEvent.IngredientAdded -> {
                val ingredientsUpdated = mutableListOf(event.ingredient).apply {
                    addAll(state.ingredients.map { it.copyWithSeenStatus() })
                }
                State(state.copyState(ingredients = ingredientsUpdated))
            }
            is IngredientsViewEvent.IngredientUpdated -> {
                val ingredientsUpdated = state.ingredients.map {
                    if (it.id == event.ingredient.id) {
                        event.ingredient
                    } else {
                        it.copyWithSeenStatus()
                    }
                }
                State(state.copyState(ingredients = ingredientsUpdated))
            }
            IngredientsViewEvent.ClickRefresh -> {
                Effect(IngredientsViewEffect.LoadData)
            }
            IngredientsViewEvent.ClickCloseSearch -> {
                Effect(IngredientsViewEffect.LoadData)
            }
            IngredientsViewEvent.EndOfListReached -> {
                Effect(IngredientsViewEffect.LoadMoreData)
            }
            IngredientsViewEvent.LoadingNext -> {
                State(state.copyState(loading = IngredientsViewState.Loading.LOADING_NEXT))
            }
            IngredientsViewEvent.LoadingNextError -> {
                State(state.copyState(error = IngredientsViewState.Error.LOADING_NEXT_ERROR))
            }
        }
    }
}
