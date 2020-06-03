package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.databinding.ViewChipSingleSelectionBinding
import com.astutify.mealplanner.coreui.presentation.control.chip.ChipChoice
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipSingleSelectionView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private lateinit var listener: ChipSingleSelectionViewListener
    private val view =
        ViewChipSingleSelectionBinding.inflate(LayoutInflater.from(context), this, true)

    override fun onFinishInflate() {
        super.onFinishInflate()
        view.group.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {
                listener.ChipSelected(it.tag)
            }
        }
    }

    fun setListener(listener: ChipSingleSelectionViewListener) {
        this.listener = listener
    }

    fun setOptions(options: List<Any>) {
        options.forEach {
            val chip = ChipChoice(context)
            chip.tag = it
            chip.text = it.toString()
            view.group.addView(chip)
        }
        setSelected(options.first())
    }

    fun setSelected(item: Any) {
        view.group.findViewWithTag<Chip>(item)?.let {
            if (view.group.checkedChipId != it.id) {
                view.group.check(it.id)
            }
        }
    }

    fun getSelected(): Any {
        return view.group.findViewById<Chip>(view.group.checkedChipId).tag
    }

    fun setOnCheckedChangeListener(listener: ChipGroup.OnCheckedChangeListener) {
        view.group.setOnCheckedChangeListener(listener)
    }

    interface ChipSingleSelectionViewListener {
        fun ChipSelected(item: Any)
    }
}
