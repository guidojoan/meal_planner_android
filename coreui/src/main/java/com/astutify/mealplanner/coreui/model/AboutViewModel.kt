package com.astutify.mealplanner.coreui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class

AboutViewModel(
    val about: String,
    val backendVersion: String
) : Parcelable
