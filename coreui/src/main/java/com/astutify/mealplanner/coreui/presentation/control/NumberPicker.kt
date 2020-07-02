package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.R
import com.astutify.mealplanner.coreui.databinding.ViewNumberPickerBinding

class NumberPicker(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val view = ViewNumberPickerBinding.inflate(LayoutInflater.from(context), this, true)
    private var titleText: String? = null
    private var listener: ((Int) -> Unit)? = null
    private var value = 0

    init {
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker)
        titleText = attributeArray.getString(R.styleable.NumberPicker_pickerTitle)
        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        view.title.text = titleText
        view.remove.setOnClickListener {
            remove()
        }
        view.add.setOnClickListener {
            add()
        }
        updateValue()
    }

    fun setValue(value: Int) {
        this.value = value
        updateValue()
    }

    private fun remove() {
        value--
        updateValue()
        listener?.invoke(value)
    }

    private fun add() {
        value++
        updateValue()
        listener?.invoke(value)
    }

    private fun updateValue() {
        view.valueView.text = value.toString()
    }

    fun setListener(listener: (Int) -> Unit) {
        this.listener = listener
    }
}
