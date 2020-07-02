package com.astutify.mealplanner.coreui.presentation.control

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun ExtendedFloatingActionButton.hideButton() {
    this.animate()
        .scaleX(0f)
        .scaleY(0f)
        .setDuration(300)
        .withEndAction { isClickable = false }
        .start()
}

fun ExtendedFloatingActionButton.showButton() {
    this.animate()
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(300)
        .withEndAction { isClickable = true }
        .start()
}
