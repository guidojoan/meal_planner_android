package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.R
import com.astutify.mealplanner.coreui.databinding.ViewChipItemsBinding
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import com.astutify.mealplanner.coreui.model.NameQuantity
import com.astutify.mealplanner.coreui.model.QuantityFormatter
import com.astutify.mealplanner.coreui.presentation.control.chip.ChipEntry

class ChipCollection(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var view = ViewChipItemsBinding.inflate(LayoutInflater.from(context), this, true)
    private var titleText: String? = null
    private var listener: ((Event) -> Unit)? = null
    private var items: List<NameQuantity> = emptyList()
    private var primaryMeasurement: MeasurementViewModel? = null

    init {
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ChipCollection)
        titleText = attributeArray.getString(R.styleable.ChipCollection_listTitle)
        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        view.title.text = titleText
        view.addButton.setOnClickListener {
            listener?.invoke(Event.OnAddClicked)
        }
    }

    fun render(newItems: List<NameQuantity>, newMeasurement: MeasurementViewModel?) {
        if (items != newItems || primaryMeasurement != newMeasurement) {
            items = newItems
            primaryMeasurement = newMeasurement
            view.group.removeAllViews()
            items.forEach {
                add(it, primaryMeasurement)
            }
        }
    }

    fun addEnabled(enabled: Boolean) {
        view.addButton.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    fun setListener(listener: (Event) -> Unit) {
        this.listener = listener
    }

    private fun add(item: NameQuantity, measurement: MeasurementViewModel?) {
        val chip = ChipEntry(context)
        chip.tag = item
        chip.text = getName(item, measurement)
        chip.setOnCloseIconClickListener {
            listener?.invoke(
                Event.OnRemoveClicked(
                    it.tag
                )
            )
        }
        view.group.addView(chip)
    }

    private fun getName(item: NameQuantity, measurement: MeasurementViewModel?): String {
        return "${item.name} ${QuantityFormatter.formatQuantity(item.quantity, measurement)}"
    }

    sealed class Event {
        object OnAddClicked : Event()
        class OnRemoveClicked(val item: Any) : Event()
    }
}
