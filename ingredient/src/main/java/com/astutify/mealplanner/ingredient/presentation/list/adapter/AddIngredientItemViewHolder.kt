package com.astutify.mealplanner.ingredient.presentation.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.ingredient.databinding.ItemAddIngredientBinding

class AddIngredientItemViewHolder(val view: ItemAddIngredientBinding) :
    RecyclerView.ViewHolder(view.root) {

    fun bind(onClick: ((IngredientsListView.Event) -> Unit)?) {
        view.addIngredientButton.setOnClickListener { onClick?.invoke(IngredientsListView.Event.AddIngredientClicked) }
    }
}
