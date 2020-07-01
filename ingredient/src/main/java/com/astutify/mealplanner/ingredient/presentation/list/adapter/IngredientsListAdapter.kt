package com.astutify.mealplanner.ingredient.presentation.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.ingredient.databinding.ItemAddIngredientBinding
import com.astutify.mealplanner.ingredient.databinding.ItemIngredientBinding

class IngredientsListAdapter :
    ListAdapter<IngredientsListView.IngredientListItem, RecyclerView.ViewHolder>(diffCallback) {

    private var eventListener: ((IngredientsListView.Event) -> Unit)? = null

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<IngredientsListView.IngredientListItem>() {
                override fun areItemsTheSame(
                    oldItem: IngredientsListView.IngredientListItem,
                    newItem: IngredientsListView.IngredientListItem
                ): Boolean = oldItem.id == newItem.id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: IngredientsListView.IngredientListItem,
                    newItem: IngredientsListView.IngredientListItem
                ): Boolean = oldItem == newItem
            }
    }

    fun setEventsListener(eventListener: (IngredientsListView.Event) -> Unit) {
        this.eventListener = eventListener
    }

    fun setItems(data: List<IngredientsListView.IngredientListItem>) {
        submitList(data)
    }

    fun clear() {
        submitList(emptyList())
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is IngredientsListView.IngredientItem -> ViewType.INGREDIENT.ordinal
            is IngredientsListView.AddIngredient -> ViewType.ADD_INGREDIENT.ordinal
            else -> throw Throwable("the list items must implement IngredientListItem")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.INGREDIENT -> {
                IngredientItemViewHolder(
                    ItemIngredientBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ViewType.ADD_INGREDIENT -> {
                AddIngredientItemViewHolder(
                    ItemAddIngredientBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientItemViewHolder -> holder.bind(
                getItem(position) as IngredientsListView.IngredientItem,
                eventListener
            )
            is AddIngredientItemViewHolder -> holder.bind(eventListener)
        }
    }

    enum class ViewType {
        INGREDIENT,
        ADD_INGREDIENT
    }
}
