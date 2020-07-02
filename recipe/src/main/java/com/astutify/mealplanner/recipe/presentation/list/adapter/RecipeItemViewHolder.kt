package com.astutify.mealplanner.recipe.presentation.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.recipe.databinding.ListItemRecipeBinding
import com.bumptech.glide.Glide

class RecipeItemViewHolder(val view: ListItemRecipeBinding) : RecyclerView.ViewHolder(view.root) {

    fun bind(item: RecipeViewModel, eventsListener: ((RecipesGridView.Event) -> Unit)?) {
        view.name.text = item.name

        view.card.setOnClickListener {
            eventsListener?.invoke(RecipesGridView.Event.ItemClick(item))
        }
        view.card.setOnLongClickListener {
            eventsListener?.invoke(RecipesGridView.Event.ItemLongClick(item))
            true
        }

        if (item.status != RecipeViewModel.Status.SEEN) {
            view.highlightView.showHighlightEffect()
        }

        Glide.with(view.root.context)
            .load(item.imageUrl)
            .centerCrop()
            .into(view.recipeImage)
    }
}
