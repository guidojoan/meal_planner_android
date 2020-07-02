package com.astutify.mealplanner.ingredient.presentation.editingredient

import com.astutify.mealplanner.coreui.model.IngredientPackageViewModel
import com.astutify.mealplanner.coreui.presentation.mvi.Effect
import com.astutify.mealplanner.coreui.presentation.mvi.Reducer
import com.astutify.mealplanner.coreui.presentation.mvi.ReducerResult
import com.astutify.mealplanner.coreui.presentation.mvi.State
import javax.inject.Inject

class EditIngredientViewReducer @Inject constructor() :
    Reducer<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect>() {

    override fun invoke(
        state: EditIngredientViewState,
        event: EditIngredientViewEvent
    ): ReducerResult<EditIngredientViewState, EditIngredientViewEffect> {
        return when (event) {
            is EditIngredientViewEvent.LoadData -> Effect(
                EditIngredientViewEffect.LoadData
            )
            is EditIngredientViewEvent.Save -> Effect(
                EditIngredientViewEffect.Save
            )
            is EditIngredientViewEvent.Loading -> State(
                state.copyState(
                    loading = EditIngredientViewState.Loading.LOADING
                )
            )
            EditIngredientViewEvent.LoadingSave -> {
                State(
                    state.copyState(
                        loading = EditIngredientViewState.Loading.LOADING_SAVE
                    )
                )
            }
            is EditIngredientViewEvent.DataLoaded -> State(
                state.copyState(
                    measurements = event.measurements,
                    customMeasurements = event.customMeasurements
                        .filterNot {
                            it in state.ingredient.getCustomMeasurements()
                                .map { item -> item.measurement }
                        },
                    categories = event.categories
                )
            )
            is EditIngredientViewEvent.LoadingError -> State(
                state.copyState(
                    error = EditIngredientViewState.Error.ERROR_LOADING
                )
            )
            is EditIngredientViewEvent.ErrorSave -> {
                State(
                    state.copyState(
                        error = EditIngredientViewState.Error.ERROR_SAVE
                    )
                )
            }
            is EditIngredientViewEvent.MeasurementSelected -> {
                val ingredientUpdated = state.ingredient.copyWithMeasurement(event.measurement)
                State(
                    state.copyState(
                        ingredient = ingredientUpdated,
                        saveEnabled = ingredientUpdated.isReadyToSave()
                    )
                )
            }
            is EditIngredientViewEvent.CategorySelected -> {
                val ingredientUpdated = state.ingredient.copyWithCategory(event.category)
                State(
                    state.copyState(
                        ingredient = ingredientUpdated,
                        saveEnabled = ingredientUpdated.isReadyToSave()
                    )
                )
            }
            is EditIngredientViewEvent.NameChanged -> {
                val ingredientUpdated = state.ingredient.copyWithName(event.name)
                State(
                    state = state.copyState(
                        ingredient = ingredientUpdated,
                        saveEnabled = ingredientUpdated.isReadyToSave()
                    )
                )
            }
            is EditIngredientViewEvent.PackageAdded -> {
                val ingredientUpdated = state.ingredient.copyWithPackage(
                    IngredientPackageViewModel(
                        name = event.name,
                        quantity = event.quantity
                    )
                )
                State(
                    state = state.copy(
                        ingredient = ingredientUpdated,
                        saveEnabled = ingredientUpdated.isReadyToSave()
                    )
                )
            }
            is EditIngredientViewEvent.PackageRemoved -> {
                val ingredientUpdated = state.ingredient.copyWitOutPackage(event.id)
                State(
                    state = state.copy(
                        ingredient = ingredientUpdated,
                        saveEnabled = ingredientUpdated.isReadyToSave()
                    )
                )
            }
            EditIngredientViewEvent.ClickBack -> {
                Effect(EditIngredientViewEffect.GoBack)
            }
            EditIngredientViewEvent.ErrorNameTaken -> {
                State(state.copyState(error = EditIngredientViewState.Error.ERROR_NAME_TAKEN))
            }
            EditIngredientViewEvent.ClickAddCustomMeasurement -> {
                State(state.copyState(dialog = EditIngredientViewState.Dialog.ADD_CUSTOM_MEASUREMENT))
            }
            is EditIngredientViewEvent.CustomMeasurementAdded -> {
                State(
                    state.copyState(
                        ingredient = state.ingredient.copyWithCustomMeasurement(
                            event.measurement,
                            event.quantity
                        ),
                        customMeasurements = state.customMeasurements.filterNot { it.id == event.measurement.id }
                    )
                )
            }
            is EditIngredientViewEvent.CustomMeasurementRemoved -> {
                State(
                    state.copyState(
                        ingredient = state.ingredient.copyWithOutCustomMeasurement(event.measurement.id),
                        customMeasurements = state.customMeasurements.toMutableList().apply {
                            add(
                                event.measurement
                            )
                        }
                    )
                )
            }
        }
    }
}
