package com.astutify.mealplanner.recipe.presentation.edit.adapter

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.model.QuantityFormatter
import com.astutify.mealplanner.coreui.presentation.recyclerview.SlideViewHolder
import com.astutify.mealplanner.recipe.databinding.ListItemRecipeIngredientBinding

class RecipeIngredientItemViewHolder(val view: ListItemRecipeIngredientBinding) :
    RecyclerView.ViewHolder(view.root),
    SlideViewHolder {

    fun bind(item: RecipeIngredientsListView.RecipeIngredientItem) {
        view.name.text = item.recipeIngredient.ingredient.name
        view.quantity.text = QuantityFormatter.formatQuantity(
            item.recipeIngredient.quantity,
            item.recipeIngredient.measurement
        )
    }
}
