package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.presentation.recyclerview.SlideAdapter
import com.astutify.mealplanner.recipe.R

class RecipeIngredientsListAdapter(
    private val onClick: (RecipeIngredientsListView.Event) -> Unit,
    private val editable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    SlideAdapter {

    var list: List<RecipeIngredientsListView.RecipeIngredientListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is RecipeIngredientsListView.RecipeIngredientItem -> ViewType.RECIPE_INGREDIENT.ordinal
            is RecipeIngredientsListView.IngredientGroupItem -> ViewType.INGREDIENT_GROUP.ordinal
            is RecipeIngredientsListView.AddIngredientGroupItem -> ViewType.ADD_INGREDIENT_GROUP.ordinal
            else -> throw Throwable("the list items must implement RecipeIngredientListItem")
        }
    }

    override fun delete(position: Int) {
        when (val item = list[position]) {
            is RecipeIngredientsListView.RecipeIngredientItem -> {
                onClick.invoke(
                    RecipeIngredientsListView.Event.RemoveIngredient(
                        item.recipeIngredient.id
                    )
                )
            }
            is RecipeIngredientsListView.IngredientGroupItem -> {
                onClick.invoke(
                    RecipeIngredientsListView.Event.RemoveIngredientGroup(
                        item.ingredientGroup.id
                    )
                )
            }
            else -> {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.INGREDIENT_GROUP -> {
                IngredientGroupItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_ingredient_group,
                        parent,
                        false
                    )
                )
            }
            ViewType.RECIPE_INGREDIENT -> {
                RecipeIngredientItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_recipe_ingredient,
                        parent,
                        false
                    )
                )
            }
            ViewType.ADD_INGREDIENT_GROUP -> {
                AddIngredientGroupItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_add_ingredient_group,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientGroupItemViewHolder -> holder.bind(
                list[position] as RecipeIngredientsListView.IngredientGroupItem,
                onClick,
                editable
            )
            is RecipeIngredientItemViewHolder -> holder.bind(
                list[position] as RecipeIngredientsListView.RecipeIngredientItem
            )
            is AddIngredientGroupItemViewHolder -> holder.bind(onClick)
            else -> throw RuntimeException("Unknown viewHolder type $holder")
        }
    }

    enum class ViewType {
        INGREDIENT_GROUP,
        RECIPE_INGREDIENT,
        ADD_INGREDIENT_GROUP
    }
}
