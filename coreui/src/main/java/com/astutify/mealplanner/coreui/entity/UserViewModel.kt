package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class

UserViewModel(
    val userId: String,
    val email: String?,
    val name: String
) : Parcelable
