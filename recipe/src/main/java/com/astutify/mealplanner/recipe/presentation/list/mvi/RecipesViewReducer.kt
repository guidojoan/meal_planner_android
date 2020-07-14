package com.astutify.mealplanner.recipe.presentation.list.mvi

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class RecipesViewReducer @Inject constructor() :
    Reducer<RecipesViewState, RecipesViewEvent, RecipesViewEffect>() {

    override fun invoke(
        state: RecipesViewState,
        event: RecipesViewEvent
    ): ReducerResult<RecipesViewState, RecipesViewEffect> {
        return when (event) {
            is RecipesViewEvent.CheckLoginStatus -> Effect(
                effect = RecipesViewEffect.CheckLoginStatus
            )
            is RecipesViewEvent.Load -> Effect(
                effect = RecipesViewEffect.LoadData
            )
            is RecipesViewEvent.LoadingError -> State(
                state = state.copyState(
                    error = RecipesViewState.Error.LOADING_ERROR
                )
            )
            is RecipesViewEvent.DataLoaded -> State(
                state = state.copyState(
                    recipes = event.recipes,
                    error = if (event.recipes.isEmpty()) RecipesViewState.Error.NO_RESULTS else null
                )
            )
            is RecipesViewEvent.ClickAddRecipe -> Effect(
                effect = RecipesViewEffect.GoToAddRecipe
            )
            is RecipesViewEvent.NetworkError -> State(
                state.copyState(
                    error = RecipesViewState.Error.LOADING_ERROR
                )
            )
            is RecipesViewEvent.Loading -> State(
                state.copyState(
                    loading = RecipesViewState.Loading.LOADING
                )
            )
            is RecipesViewEvent.RecipeClicked -> {
                Effect(
                    RecipesViewEffect.GoToRecipeDetail(
                        event.recipe
                    )
                )
            }
            is RecipesViewEvent.RecipeAdded -> {
                val recipesUpdated = mutableListOf(event.recipe).apply {
                    addAll(state.recipes.map { it.copyWithSeenStatus() })
                }
                State(state.copyState(recipes = recipesUpdated))
            }
            is RecipesViewEvent.RecipeLongClicked -> {
                Effect(
                    RecipesViewEffect.GoToEditRecipe(
                        event.recipe
                    )
                )
            }
            is RecipesViewEvent.RecipeUpdated -> {
                val recipesUpdated = state.recipes.map {
                    if (it.id == event.recipe.id) {
                        event.recipe
                    } else {
                        it.copyWithSeenStatus()
                    }
                }
                State(state.copyState(recipes = recipesUpdated))
            }
            is RecipesViewEvent.RecipeDeleted -> {
                val recipesUpdated = state.recipes.filterNot { it.id == event.recipe.id }
                State(
                    state.copyState(
                        recipes = recipesUpdated,
                        error = if (recipesUpdated.isEmpty()) RecipesViewState.Error.NO_RESULTS else null
                    )
                )
            }
            RecipesViewEvent.ClickRefresh -> {
                Effect(RecipesViewEffect.LoadData)
            }
            RecipesViewEvent.ClickCloseSearch -> {
                Effect(RecipesViewEffect.LoadData)
            }
            is RecipesViewEvent.Search -> {
                Effect(
                    RecipesViewEffect.Search(
                        event.name
                    )
                )
            }
            RecipesViewEvent.LoadingNextError -> {
                State(state.copyState(error = RecipesViewState.Error.LOADING_NEXT_ERROR))
            }
            RecipesViewEvent.LoadingNext -> {
                State(state.copyState(loading = RecipesViewState.Loading.LOADING_NEXT))
            }
            is RecipesViewEvent.NextDataLoaded -> {
                State(state.copyState(recipes = state.recipes.toMutableList().apply { addAll(event.recipes) }))
            }
            RecipesViewEvent.EndOfListReached -> {
                Effect(RecipesViewEffect.EndOfListReached)
            }
        }
    }
}
