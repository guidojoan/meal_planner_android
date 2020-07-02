package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.databinding.ViewChipSingleSelectionBinding
import com.astutify.mealplanner.coreui.presentation.control.chip.ChipChoice
import com.google.android.material.chip.Chip

class ChipCollectionSingleSelection(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val view =
        ViewChipSingleSelectionBinding.inflate(LayoutInflater.from(context), this, true)
    private var items: List<Any> = listOf()

    fun setListener(listener: (Any?) -> Unit) {
        view.group.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {
                listener(it.tag)
            }
        }
    }

    fun setOptions(options: List<Any>, selected: Any? = null) {
        if (items != options) {
            items = options
            view.group.removeAllViews()
            options.forEach {
                val chip = ChipChoice(context)
                chip.tag = it
                chip.text = it.toString()
                view.group.addView(chip)
            }
            setSelected(selected)
        }
    }

    fun hasItems() = items.isNotEmpty()

    private fun setSelected(item: Any?) {
        view.group.findViewWithTag<Chip>(item)?.let {
            if (view.group.checkedChipId != it.id) {
                view.group.check(it.id)
            }
        }
    }

    fun getSelected(): Any {
        return view.group.findViewById<Chip>(view.group.checkedChipId).tag
    }
}
