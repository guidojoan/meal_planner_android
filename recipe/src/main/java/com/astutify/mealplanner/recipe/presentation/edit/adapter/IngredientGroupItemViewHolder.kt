package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.presentation.recyclerview.SlideViewHolder
import com.astutify.mealplanner.recipe.databinding.ListItemIngredientGroupBinding

class IngredientGroupItemViewHolder(val view: ListItemIngredientGroupBinding) :
    RecyclerView.ViewHolder(view.root),
    SlideViewHolder {

    fun bind(
        item: RecipeIngredientsListView.IngredientGroupItem,
        onUserClick: (RecipeIngredientsListView.Event) -> Unit,
        editable: Boolean
    ) {
        view.name.text = item.ingredientGroup.name
        if (editable) {
            view.addButton.setOnClickListener {
                onUserClick(RecipeIngredientsListView.Event.AddIngredient(item.ingredientGroup.id))
            }
        } else {
            view.addButton.visibility = View.GONE
        }
    }
}
