package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.presentation.QuantityFormatter
import com.astutify.mealplanner.coreui.presentation.recyclerview.SlideViewHolder
import com.astutify.mealplanner.recipe.R

class RecipeIngredientItemViewHolder(view: View) :
    RecyclerView.ViewHolder(view),
    SlideViewHolder {

    private val name: TextView = view.findViewById(R.id.name)
    private val quantity: TextView = view.findViewById(R.id.quantity)

    fun bind(item: RecipeIngredientsListView.RecipeIngredientItem) {
        name.text = item.recipeIngredient.ingredient.name
        quantity.text = QuantityFormatter.formatQuantity(
            item.recipeIngredient.quantity,
            item.recipeIngredient.measurement
        )
    }
}
