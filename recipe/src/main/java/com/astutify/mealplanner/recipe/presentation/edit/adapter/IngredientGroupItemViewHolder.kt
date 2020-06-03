package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.presentation.recyclerview.SlideViewHolder
import com.astutify.mealplanner.recipe.R

class IngredientGroupItemViewHolder(view: View) :
    RecyclerView.ViewHolder(view),
    SlideViewHolder {

    private val name: TextView = view.findViewById(R.id.name)
    private val addButton: ImageView = view.findViewById(R.id.addButton)

    fun bind(
        item: RecipeIngredientsListView.IngredientGroupItem,
        onUserClick: (RecipeIngredientsListView.Event) -> Unit,
        editable: Boolean
    ) {
        name.text = item.ingredientGroup.name
        if (editable) {
            addButton.setOnClickListener {
                onUserClick(RecipeIngredientsListView.Event.AddIngredient(item.ingredientGroup.id))
            }
        } else {
            addButton.visibility = View.GONE
        }
    }
}
