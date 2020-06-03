package com.astutify.mealplanner.recipe.presentation.list.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.view.HighlightView
import com.astutify.mealplanner.recipe.R
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class RecipeItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val card: MaterialCardView = view.findViewById(R.id.card)
    private val name: TextView = view.findViewById(R.id.name)
    private val highlightView: HighlightView = view.findViewById(R.id.highlightView)
    private val recipeImage: ImageView = view.findViewById(R.id.recipeImage)

    fun bind(item: RecipeViewModel, eventsListener: ((RecipesGridView.Event) -> Unit)?) {
        name.text = item.name

        card.setOnClickListener {
            eventsListener?.invoke(RecipesGridView.Event.ItemClick(item))
        }
        card.setOnLongClickListener {
            eventsListener?.invoke(RecipesGridView.Event.ItemLongClick(item))
            true
        }

        if (item.status != RecipeViewModel.Status.SEEN) {
            highlightView.showHighlightEffect()
        }

        Glide.with(view.context)
            .load(item.imageUrl)
            .centerCrop()
            .into(recipeImage)
    }
}
