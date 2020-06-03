package com.astutify.mealplanner.coreui.entity

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import com.astutify.mealplanner.coreui.presentation.QuantityFormatter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MeasurementViewModel(
    val id: String = String.EMPTY,
    val name: String = String.EMPTY,
    val primary: Boolean = true,
    val imperialSuffix: String = String.EMPTY,
    val metricSuffix: String = String.EMPTY,
    val imperial1000Suffix: String? = null,
    val metric1000Suffix: String? = null,
    val imperialSuffixPlural: String? = null,
    val metricSuffixPlural: String? = null,
    val imperial1000SuffixPlural: String? = null,
    val metric1000SuffixPlural: String? = null
) : Parcelable {

    override fun toString() = name

    fun getSuffix() = QuantityFormatter.getSuffix(this)
}
