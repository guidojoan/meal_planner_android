package com.astutify.mealplanner.ingredient.presentation.list.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.view.HighlightView
import com.astutify.mealplanner.coreui.utils.UIUtils
import com.astutify.mealplanner.ingredient.R
import com.google.android.material.card.MaterialCardView

class IngredientItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.name)
    private val letterContent: TextView = view.findViewById(R.id.letter_content)
    private val letter: ConstraintLayout = view.findViewById(R.id.letter)
    private val card: MaterialCardView = view.findViewById(R.id.card)
    private val highlightView: HighlightView = view.findViewById(R.id.highlightView)

    init {
        letter.setBackgroundColor(UIUtils.getRandomColor(view.context))
    }

    fun bind(
        item: IngredientsListView.IngredientItem,
        onClick: ((IngredientsListView.Event) -> Unit)?
    ) {
        name.text = item.ingredient.name
        letterContent.text = item.ingredient.name.take(1)
        card.setOnClickListener { onClick?.invoke(IngredientsListView.Event.IngredientClicked(item.ingredient)) }

        if (item.ingredient.status != IngredientViewModel.Status.SEEN) {
            highlightView.showHighlightEffect()
        }
    }
}
