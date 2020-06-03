package com.astutify.mealplanner.ingredient.presentation.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.ingredient.R
import com.google.android.material.button.MaterialButton

class AddIngredientItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val addIngredientButton: MaterialButton = view.findViewById(R.id.addIngredientButton)

    fun bind(onClick: ((IngredientsListView.Event) -> Unit)?) {
        addIngredientButton.setOnClickListener { onClick?.invoke(IngredientsListView.Event.AddIngredientClicked) }
    }
}
