package com.astutify.mealplanner.ingredient.presentation.list.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.core.extension.EMPTY
import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.recyclerview.EndlessRecyclerViewScrollListener
import com.astutify.mealplanner.ingredient.R

class IngredientsListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var editable: Boolean = false
    private var manager: LinearLayoutManager
    private var paginationListener: EndlessRecyclerViewScrollListener
    var eventsListener: ((Event) -> Unit)? = null

    init {
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
        manager = LinearLayoutManager(context)
        manager.orientation = VERTICAL
        layoutManager = manager
        adapter = IngredientsListAdapter()
        paginationListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                eventsListener?.invoke(Event.OnEndReached)
            }
        }
        addOnScrollListener(paginationListener)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.IngredientsListView)
        editable = attributeArray.getBoolean(R.styleable.IngredientsListView_editableAction, false)
        attributeArray.recycle()
    }

    fun clear() {
        getListAdapter().clear()
        paginationListener.resetState()
    }

    fun render(ingredients: List<IngredientViewModel>) {
        getListAdapter().setItems(mapList(ingredients))
    }

    fun bind(
        eventsListener: (Event) -> Unit
    ) {
        this.eventsListener = eventsListener
        getListAdapter().setEventsListener(eventsListener)
    }

    private fun getListAdapter(): IngredientsListAdapter {
        return adapter as IngredientsListAdapter
    }

    private fun mapList(list: List<IngredientViewModel>): List<IngredientListItem> {
        val result = mutableListOf<IngredientListItem>()
        list.forEach {
            result.add(IngredientItem(it))
        }
        if (editable) {
            result.add(AddIngredient())
        }
        return result
    }

    fun scrollToTop() {
        manager.scrollToPosition(0)
    }

    interface IngredientListItem {
        val id: String
    }

    class AddIngredient(
        override val id: String = String.EMPTY
    ) : IngredientListItem

    data class IngredientItem(
        val ingredient: IngredientViewModel,
        override val id: String = ingredient.id
    ) : IngredientListItem

    sealed class Event {
        data class IngredientClicked(val ingredient: IngredientViewModel) : Event()
        object AddIngredientClicked : Event()
        object OnEndReached : Event()
    }
}
