package com.astutify.mealplanner.coreui.presentation.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.astutify.mealplanner.coreui.R

class HighlightView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_highlight, this, true)
        visibility = View.GONE
    }

    fun showHighlightEffect() {
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(MEDIUM_DURATION)
            .alpha(0f)
            .setDuration(MEDIUM_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    View.GONE
                }
            })
    }

    companion object {
        private const val MEDIUM_DURATION = 1500L
    }
}
