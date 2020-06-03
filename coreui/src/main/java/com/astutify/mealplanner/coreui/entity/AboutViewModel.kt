package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class

AboutViewModel(
    val about: String,
    val backendVersion: String
) : Parcelable
