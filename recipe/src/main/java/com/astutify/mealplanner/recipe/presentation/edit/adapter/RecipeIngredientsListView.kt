package com.astutify.mealplanner.recipe.presentation.edit.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.model.IngredientGroupViewModel
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
import com.astutify.mealplanner.coreui.presentation.recyclerview.SwipeToDeleteCallback
import com.astutify.mealplanner.recipe.R

class RecipeIngredientsListView : RecyclerView {

    private var editable: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val manager = LinearLayoutManager(context)
        manager.orientation = VERTICAL
        layoutManager = manager
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray =
            context.obtainStyledAttributes(attrs, R.styleable.RecipeIngredientsListView)
        editable =
            attributeArray.getBoolean(R.styleable.RecipeIngredientsListView_editableAction, false)
        attributeArray.recycle()
    }

    fun bind(
        list: List<IngredientGroupViewModel>,
        onClick: (Event) -> Unit
    ) {
        val ingredientsAdapter = adapter as RecipeIngredientsListAdapter?
            ?: RecipeIngredientsListAdapter(onClick, editable)
        ingredientsAdapter.list = mapList(list)
        if (adapter == null) {
            adapter = ingredientsAdapter
        }

        if (editable) {
            val itemTouchHelper =
                ItemTouchHelper(
                    SwipeToDeleteCallback(
                        ingredientsAdapter,
                        context
                    )
                )
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun mapList(list: List<IngredientGroupViewModel>): List<RecipeIngredientListItem> {
        val result = mutableListOf<RecipeIngredientListItem>()
        list.forEach { ingredientGroup ->
            result.add(IngredientGroupItem(ingredientGroup))
            ingredientGroup.recipeIngredients.forEach { recipeIngredient ->
                result.add(RecipeIngredientItem(recipeIngredient, ingredientGroup.id))
            }
        }
        if (editable) {
            result.add(AddIngredientGroupItem())
        }
        return result
    }

    interface RecipeIngredientListItem

    class AddIngredientGroupItem : RecipeIngredientListItem

    data class RecipeIngredientItem(
        val recipeIngredient: RecipeIngredientViewModel,
        val ingredientGroupId: String
    ) : RecipeIngredientListItem

    data class IngredientGroupItem(
        val ingredientGroup: IngredientGroupViewModel
    ) : RecipeIngredientListItem

    sealed class Event {
        class AddIngredient(val ingredientGroupId: String) : Event()
        object AddIngredientGroup : Event()
        class RemoveIngredient(val ingredientId: String) : Event()
        class RemoveIngredientGroup(val ingredientGroupId: String) : Event()
    }
}
