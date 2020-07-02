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

class ItemWithMeasurementCollection(context: Context, val attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var view = ViewChipItemsBinding.inflate(LayoutInflater.from(context), this, true)
    private var listener: ((Event) -> Unit)? = null
    private var items: List<NameQuantity> = emptyList()
    private var measurement: MeasurementViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        getTitleText()
        view.addButton.setOnClickListener {
            listener?.invoke(Event.OnAddClicked)
        }
    }

    private fun getTitleText() {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ItemWithMeasurementCollection)
        view.title.text = attributeArray.getString(R.styleable.ItemWithMeasurementCollection_listTitle)?.let { "" }
        attributeArray.recycle()
    }

    fun render(newItems: List<NameQuantity>, newMeasurement: MeasurementViewModel?) {
        if (items != newItems || measurement != newMeasurement) {
            items = newItems
            measurement = newMeasurement
            view.group.removeAllViews()
            items.forEach {
                add(it, measurement)
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
            listener?.invoke(Event.OnRemoveClicked(it.tag))
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
