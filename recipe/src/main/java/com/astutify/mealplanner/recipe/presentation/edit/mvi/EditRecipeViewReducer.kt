package com.astutify.mealplanner.recipe.presentation.edit.mvi

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class EditRecipeViewReducer @Inject constructor() :
    Reducer<EditRecipeViewState, EditRecipeViewEvent, EditRecipeViewEffect>() {

    override fun invoke(
        state: EditRecipeViewState,
        event: EditRecipeViewEvent
    ): ReducerResult<EditRecipeViewState, EditRecipeViewEffect> {
        return when (event) {
            is EditRecipeViewEvent.DirectionsChanged -> {
                val newRecipeSate =
                    state.recipe.copyWithDirections(event.directions)
                State(
                    state = state.copyState(
                        recipe = newRecipeSate,
                        saveEnabled = newRecipeSate.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.NameChanged -> {
                val newRecipeSate = state.recipe.copyWithName(event.name)
                State(
                    state = state.copyState(
                        recipe = newRecipeSate,
                        saveEnabled = newRecipeSate.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.ServingsChanged -> {
                val newRecipeSate = state.recipe.copyWithServings(event.servings)
                State(
                    state = state.copyState(
                        recipe = newRecipeSate,
                        saveEnabled = newRecipeSate.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.IngredientAdded -> {
                val newRecipeSate =
                    state.recipe.copyWithIngredient(event.ingredientGroupId, event.recipeIngredient)
                State(
                    state = state.copyState(
                        recipe = newRecipeSate,
                        saveEnabled = newRecipeSate.saveEnabled()
                    )
                )
            }
            EditRecipeViewEvent.ClickSave -> {
                return if (state.recipe.imageUrl.isEmpty()) {
                    State(
                        state.copyState(
                            error = EditRecipeViewState.Error.IMAGE_NOT_SELECTED
                        )
                    )
                } else {
                    Effect(
                        effect = EditRecipeViewEffect.SaveRecipe(
                            state.recipe
                        )
                    )
                }
            }
            is EditRecipeViewEvent.NewIngredientClicked -> {
                Effect(
                    effect = EditRecipeViewEffect.GoToRecipeIngredients(
                        event.ingredientGroupId
                    )
                )
            }
            EditRecipeViewEvent.OnSaveError -> {
                State(
                    state = state.copyState(
                        error = EditRecipeViewState.Error.SAVE,
                        saveEnabled = true
                    )
                )
            }
            EditRecipeViewEvent.Loading -> {
                State(
                    state = state.copyState(
                        loading = EditRecipeViewState.Loading.LOADING,
                        saveEnabled = false
                    )
                )
            }
            is EditRecipeViewEvent.IngredientGroupAdded -> {
                val newRecipeState = state.recipe.copyWithIngredientGroup(event.name)
                State(
                    state = state.copyState(
                        recipe = newRecipeState,
                        saveEnabled = newRecipeState.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.RecipeIngredientRemoveClick -> {
                val newRecipeState = state.recipe.copyWithOutIngredient(event.recipeIngredientId)
                State(
                    state = state.copyState(
                        recipe = newRecipeState,
                        saveEnabled = newRecipeState.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.IngredientGroupRemoveClick -> {
                val newRecipeState =
                    state.recipe.copyWithOutIngredientGroup(event.ingredientGroupId)
                State(
                    state = state.copyState(
                        recipe = newRecipeState,
                        saveEnabled = newRecipeState.saveEnabled()
                    )
                )
            }
            is EditRecipeViewEvent.ImagePicked -> {
                State(
                    state = state.copyState(
                        recipe = state.recipe.copy(imageUrl = event.imageUrl)
                    )
                )
            }
            EditRecipeViewEvent.LoadData -> {
                Effect(
                    EditRecipeViewEffect.LoadData
                )
            }
            EditRecipeViewEvent.LoadingError -> {
                State(
                    state.copyState(
                        error = EditRecipeViewState.Error.LOADING
                    )
                )
            }
            is EditRecipeViewEvent.DataLoaded -> {
                State(
                    state.copyState(
                        recipeCategories = event.recipeCategories
                    )
                )
            }
            EditRecipeViewEvent.LoadingSave -> {
                State(
                    state.copyState(
                        loading = EditRecipeViewState.Loading.SAVE
                    )
                )
            }
            is EditRecipeViewEvent.CategorySelected -> {
                val newRecipeState = state.recipe.copy(recipeCategory = event.recipeCategory)
                State(
                    state.copyState(
                        recipe = newRecipeState,
                        saveEnabled = newRecipeState.saveEnabled()
                    )
                )
            }
            EditRecipeViewEvent.ClickBack -> {
                Effect(EditRecipeViewEffect.GoBack)
            }
            EditRecipeViewEvent.ClickDelete -> {
                State(
                    state.copyState(
                        message = EditRecipeViewState.Message.DELETE_ALERT
                    )
                )
            }
            EditRecipeViewEvent.ConfirmDelete -> {
                Effect(EditRecipeViewEffect.Delete)
            }
            EditRecipeViewEvent.ErrorNameTaken -> {
                State(
                    state.copyState(
                        error = EditRecipeViewState.Error.NAME_TAKEN
                    )
                )
            }
        }
    }
}
