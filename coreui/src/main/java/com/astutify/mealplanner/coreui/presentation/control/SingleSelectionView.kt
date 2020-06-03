package com.astutify.mealplanner.coreui.presentation.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mealplanner.coreui.R
import com.astutify.mealplanner.coreui.databinding.ViewSelectionChipBinding
import com.astutify.mealplanner.coreui.presentation.control.chip.ChipChoice
import com.google.android.material.chip.Chip

class SingleSelectionView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private var selectionList: List<SelectionItem> = emptyList()
    private var titleText: String? = null
    private val view = ViewSelectionChipBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.SingleSelectionView)
        titleText = attributeArray.getString(R.styleable.SingleSelectionView_title)
        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        view.title.text = titleText
    }

    fun render(options: List<SelectionItem>) {
        if (selectionList != options) {
            selectionList = options
            view.group.removeAllViews()
            selectionList.forEach {
                val chip =
                    ChipChoice(
                        context
                    )
                chip.tag = it.item
                chip.text = it.item.toString()
                view.group.addView(chip)
            }
            selectionList.find { it.selected }?.let {
                setSelected(it.item)
            }
        }
    }

    fun setSelected(item: Any) {
        view.group.findViewWithTag<Chip>(item)?.let {
            view.group.check(it.id)
        }
    }

    fun hasItems() = selectionList.isNotEmpty()

    fun setListener(listener: (Any?) -> Unit) {
        view.group.setOnCheckedChangeListener { group, checkedId ->
            val selectedChip = group.findViewById<Chip>(checkedId)
            listener(selectionList.find { it.item == selectedChip?.tag }?.item)
        }
    }

    data class SelectionItem(
        val item: Any,
        val selected: Boolean = false
    )
}
