package com.astutify.mealplanner.recipe.presentation.edit.adapter

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.recipe.databinding.ListItemAddIngredientGroupBinding

class AddIngredientGroupItemViewHolder(val view: ListItemAddIngredientGroupBinding) : RecyclerView.ViewHolder(view.root) {

    fun bind(onUserClick: (RecipeIngredientsListView.Event) -> Unit) {
        view.buttonAddIngredientGroup.setOnClickListener {
            onUserClick(RecipeIngredientsListView.Event.AddIngredientGroup)
        }
    }
}
