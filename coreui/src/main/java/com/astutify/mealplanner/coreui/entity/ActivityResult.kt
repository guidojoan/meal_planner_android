package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityResult<T : Parcelable>(
    val result: T,
    val extra: String = ""
) : Parcelable
