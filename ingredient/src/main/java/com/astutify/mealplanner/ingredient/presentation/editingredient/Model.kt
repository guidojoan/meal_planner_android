package com.astutify.mealplanner.ingredient.presentation.editingredient

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
    ) = EditIngredientViewState(
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

sealed class EditIngredientViewEffect {
    object LoadData : EditIngredientViewEffect()
    object Save : EditIngredientViewEffect()
    object GoBack : EditIngredientViewEffect()
}
