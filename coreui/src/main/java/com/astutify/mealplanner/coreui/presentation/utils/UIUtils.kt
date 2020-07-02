package com.astutify.mealplanner.coreui.presentation.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.astutify.mealplanner.coreui.R
import kotlin.random.Random

class UIUtils {

    companion object {

        fun getRandomColor(context: Context): Int {
            return when (Random.nextInt(0, 2)) {
                0 -> ContextCompat.getColor(context, R.color.orange)
                else -> ContextCompat.getColor(context, R.color.green)
            }
        }
    }
}
