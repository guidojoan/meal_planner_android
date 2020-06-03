package com.astutify.mealplanner.recipe.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.recipe.R

class RecipesAdapter : ListAdapter<RecipeViewModel, RecyclerView.ViewHolder>(diffCallback) {

    private var eventsListener: ((RecipesGridView.Event) -> Unit)? = null

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RecipeViewModel>() {
            override fun areItemsTheSame(
                oldItem: RecipeViewModel,
                newItem: RecipeViewModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: RecipeViewModel,
                newItem: RecipeViewModel
            ): Boolean = oldItem == newItem
        }
    }

    fun setItems(data: List<RecipeViewModel>) {
        submitList(data)
    }

    fun clear() {
        submitList(emptyList())
    }

    fun setListener(eventsListener: (RecipesGridView.Event) -> Unit) {
        this.eventsListener = eventsListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
        return RecipeItemViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_item_recipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecipeItemViewHolder).bind(getItem(position), eventsListener)
    }
}
