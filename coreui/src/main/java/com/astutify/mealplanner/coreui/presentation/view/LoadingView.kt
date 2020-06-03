package com.astutify.mealplanner.coreui.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.databinding.ViewLoadingBinding

class LoadingView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var view: ViewLoadingBinding =
        ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderLoading() {
        showLoading()
        show(1f)
    }

    fun renderPartialLoading() {
        showLoading()
        show(0.7f)
    }

    fun hide() {
        visibility = View.GONE
    }

    private fun show(alpha: Float) {
        view.containerBackground.alpha = alpha
        visibility = View.VISIBLE
    }

    private fun showLoading() {
        view.loading.visibility = View.VISIBLE
    }
}
