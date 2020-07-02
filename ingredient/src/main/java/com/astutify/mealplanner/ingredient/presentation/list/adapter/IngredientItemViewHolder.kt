package com.astutify.mealplanner.ingredient.presentation.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.utils.UIUtils
import com.astutify.mealplanner.ingredient.databinding.ItemIngredientBinding

class IngredientItemViewHolder(val view: ItemIngredientBinding) :
    RecyclerView.ViewHolder(view.root) {

    init {
        view.letter.setBackgroundColor(UIUtils.getRandomColor(view.root.context))
    }

    fun bind(
        item: IngredientsListView.IngredientItem,
        onClick: ((IngredientsListView.Event) -> Unit)?
    ) {
        view.name.text = item.ingredient.name
        view.letterContent.text = item.ingredient.name.take(1)
        view.card.setOnClickListener {
            onClick?.invoke(
                IngredientsListView.Event.IngredientClicked(
                    item.ingredient
                )
            )
        }

        if (item.ingredient.status != IngredientViewModel.Status.SEEN) {
            view.highlightView.showHighlightEffect()
        }
    }
}
