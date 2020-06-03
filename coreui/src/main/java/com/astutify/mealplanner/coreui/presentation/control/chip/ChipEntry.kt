package com.astutify.mealplanner.coreui.presentation.control.chip

import android.content.Context
import com.astutify.mealplanner.coreui.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class ChipEntry(context: Context) : Chip(context) {

    init {
        val chipDrawable = ChipDrawable.createFromAttributes(
            context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        setChipDrawable(chipDrawable)
        setTextAppearance(R.style.ChipTextAppeareance)
    }
}
