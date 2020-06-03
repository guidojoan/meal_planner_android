package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.recipe.R
import com.google.android.material.button.MaterialButton

class AddIngredientGroupItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val buttonAddIngredientGroup: MaterialButton = view.findViewById(R.id.buttonAddIngredientGroup)

    fun bind(onUserClick: (RecipeIngredientsListView.Event) -> Unit) {
        buttonAddIngredientGroup.setOnClickListener {
            onUserClick(RecipeIngredientsListView.Event.AddIngredientGroup)
        }
    }
}
