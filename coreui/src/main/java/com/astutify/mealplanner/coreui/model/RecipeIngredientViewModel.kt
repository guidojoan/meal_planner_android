package com.astutify.mealplanner.coreui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class RecipeIngredientViewModel(
    val id: String = UUID.randomUUID().toString(),
    val quantity: Float,
    val ingredient: IngredientViewModel,
    val measurement: MeasurementViewModel
) : Parcelable {

    fun copyWithNewServings(servings: Int, newServings: Int): RecipeIngredientViewModel {
        val newQuantity = (newServings * quantity) / servings
        return copy(quantity = newQuantity)
    }
}
