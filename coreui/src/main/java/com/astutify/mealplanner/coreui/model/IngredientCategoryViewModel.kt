package com.astutify.mealplanner.coreui.model

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientCategoryViewModel(
    val id: String = String.EMPTY,
    val name: String = String.EMPTY
) : Parcelable {

    override fun toString() = name
}
