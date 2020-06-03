package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class IngredientMeasurementViewModel(
    val id: String = UUID.randomUUID().toString(),
    override val quantity: Float = 0f,
    val measurement: MeasurementViewModel = MeasurementViewModel(),
    override val name: String = measurement.name
) : Parcelable, NameQuantity
