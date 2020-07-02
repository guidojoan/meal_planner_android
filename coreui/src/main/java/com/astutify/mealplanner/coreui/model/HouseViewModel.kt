package com.astutify.mealplanner.coreui.model

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import com.astutify.mealplanner.core.extension.ZERO
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseViewModel(
    val id: String = String.EMPTY,
    val name: String,
    val joinCode: Int = Int.ZERO
) : Parcelable {

    override fun toString() = name
}
