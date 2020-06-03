package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class IngredientPackageViewModel(
    val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val quantity: Float
) : Parcelable, NameQuantity
