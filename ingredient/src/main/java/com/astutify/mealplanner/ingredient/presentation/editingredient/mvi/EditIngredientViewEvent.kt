package com.astutify.mealplanner.ingredient.presentation.editingredient.mvi

import com.astutify.mealplanner.coreui.model.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel

sealed class EditIngredientViewEvent {
    object ClickBack : EditIngredientViewEvent()
    object Loading : EditIngredientViewEvent()
    object LoadData : EditIngredientViewEvent()
    object LoadingError : EditIngredientViewEvent()
    object Save : EditIngredientViewEvent()
    object LoadingSave : EditIngredientViewEvent()
    object ErrorSave : EditIngredientViewEvent()
    object ErrorNameTaken : EditIngredientViewEvent()
    object ClickAddCustomMeasurement : EditIngredientViewEvent()
    class DataLoaded(
        val measurements: List<MeasurementViewModel>,
        val customMeasurements: List<MeasurementViewModel>,
        val categories: List<IngredientCategoryViewModel>
    ) : EditIngredientViewEvent()

    data class MeasurementSelected(val measurement: MeasurementViewModel) :
        EditIngredientViewEvent()

    data class CategorySelected(val category: IngredientCategoryViewModel) :
        EditIngredientViewEvent()

    data class NameChanged(val name: String) : EditIngredientViewEvent()
    data class PackageAdded(val name: String, val quantity: Float) : EditIngredientViewEvent()
    data class PackageRemoved(val id: String) : EditIngredientViewEvent()
    data class CustomMeasurementAdded(val measurement: MeasurementViewModel, val quantity: Float) :
        EditIngredientViewEvent()

    data class CustomMeasurementRemoved(val measurement: MeasurementViewModel) :
        EditIngredientViewEvent()
}