package com.astutify.mealplanner.ingredient.presentation.editingredient

import com.astutify.mealplanner.coreui.model.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import io.reactivex.Observable

interface EditIngredientView {

    fun render(viewState: EditIngredientViewState)

    val events: Observable<Intent>

    sealed class Intent {
        object ClickSave : Intent()
        object ClickBack : Intent()
        object ClickAddCustomMeasurement : Intent()
        data class MeasurementSelected(val measurement: MeasurementViewModel) : Intent()
        data class CategorySelected(val category: IngredientCategoryViewModel) : Intent()
        data class NameChanged(val name: String) : Intent()
        data class PackageAdded(val name: String, val quantity: Float) : Intent()
        data class PackageRemoved(val id: String) : Intent()
        data class CustomMeasurementAdded(
            val measurement: MeasurementViewModel,
            val quantity: Float
        ) : Intent()

        data class CustomMeasurementRemoved(val measurement: MeasurementViewModel) : Intent()
    }
}
