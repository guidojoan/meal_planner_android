package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.astutify.mealplanner.coreui.presentation.view.DrawableSpan
import com.google.android.material.button.MaterialButton

class LoadingButton(context: Context, attrs: AttributeSet?) : MaterialButton(context, attrs) {

    private lateinit var buttonText: CharSequence

    override fun onFinishInflate() {
        super.onFinishInflate()
        buttonText = text
    }

    fun showLoading() {

        val progressDrawable = CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.DEFAULT)
            setColorSchemeColors(Color.WHITE)
            val size = (centerRadius + strokeWidth).toInt() * 2
            setBounds(0, 0, size, size)
        }

        val drawableSpan =
            DrawableSpan(
                progressDrawable
            )
        val spannableString = SpannableString(" ").apply {
            setSpan(drawableSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        progressDrawable.start()

        val callback = object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {}

            override fun invalidateDrawable(who: Drawable) {
                invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}
        }

        progressDrawable.callback = callback
        buttonText = text
        text = spannableString
        isClickable = false
        isEnabled = false
    }

    fun hideLoading() {
        text = buttonText
        isClickable = true
        isEnabled = true
    }
}
