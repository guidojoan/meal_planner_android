package com.astutify.mealplanner.coreui.presentation.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.astutify.mealplanner.coreui.R
import com.astutify.mealplanner.coreui.databinding.ViewMessageBinding
import com.google.android.material.snackbar.Snackbar

class MessageView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var listener: ((Event) -> Unit)? = null
    private val view = ViewMessageBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderFullScreenMessage(messageText: String, imageDrawable: Drawable) {
        showMessage(messageText, imageDrawable)
        removeListener()
    }

    fun renderSnackBarMessage(messageText: String, container: View) {
        hide()
        Snackbar.make(container, messageText, Snackbar.LENGTH_SHORT).show()
    }

    fun renderNetWorkError() {
        showMessage(
            context.getString(R.string.generic_network_error),
            ActivityCompat.getDrawable(context, R.drawable.img_patatas)
        )
        setOnClickListener {
            listener?.invoke(Event.OnRetryClick)
        }
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }

    private fun showMessage(messageString: String, imageDrawable: Drawable?) {
        view.image.setImageDrawable(imageDrawable)
        view.message.text = messageString
        visibility = View.VISIBLE
    }

    private fun removeListener() {
        setOnClickListener {}
    }

    fun setListener(listener: (Event) -> Unit) {
        this.listener = listener
    }

    sealed class Event {
        object OnRetryClick : Event()
    }
}
