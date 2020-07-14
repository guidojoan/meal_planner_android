package com.astutify.mealplanner.ingredient.presentation.editingredient.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditIngredientViewState(
    val ingredient: IngredientViewModel = IngredientViewModel(),
    val categories: List<IngredientCategoryViewModel> = emptyList(),
    val measurements: List<MeasurementViewModel> = emptyList(),
    val customMeasurements: List<MeasurementViewModel> = emptyList(),
    val saveEnabled: Boolean = false,
    val mode: Mode = Mode.NEW,
    val loading: Loading? = null,
    val error: Error? = null,
    val dialog: Dialog? = null

) : Parcelable {
    fun copyState(
        ingredient: IngredientViewModel = this.ingredient,
        categories: List<IngredientCategoryViewModel> = this.categories,
        measurements: List<MeasurementViewModel> = this.measurements,
        customMeasurements: List<MeasurementViewModel> = this.customMeasurements,
        saveEnabled: Boolean = this.saveEnabled,
        mode: Mode = this.mode,
        loading: Loading? = null,
        error: Error? = null,
        dialog: Dialog? = null
    ) =
        EditIngredientViewState(
            ingredient,
            categories,
            measurements,
            customMeasurements,
            saveEnabled,
            mode,
            loading,
            error,
            dialog
        )

    enum class Loading {
        LOADING, LOADING_SAVE
    }

    enum class Error {
        ERROR_LOADING, ERROR_SAVE, ERROR_NAME_TAKEN
    }

    enum class Dialog {
        ADD_CUSTOM_MEASUREMENT
    }

    enum class Mode {
        NEW, EDIT
    }
}