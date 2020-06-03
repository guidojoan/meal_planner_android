package com.astutify.mealplanner.recipe.presentation.list.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.recyclerview.EndlessRecyclerViewScrollListener
import com.astutify.mealplanner.coreui.presentation.recyclerview.GridSpacingItemDecoration
import com.astutify.mealplanner.recipe.R

class RecipesGridView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var spanCount: Int = 1
    private var spacing = 30
    private var includeEdge = false
    private var manager: GridLayoutManager
    private var paginationListener: EndlessRecyclerViewScrollListener
    var eventsListener: ((Event) -> Unit)? = null

    init {
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
        manager = GridLayoutManager(context, spanCount)
        manager.orientation = VERTICAL
        layoutManager = manager
        adapter = RecipesAdapter()
        addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        paginationListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                eventsListener?.invoke(Event.OnEndReached)
            }
        }
        addOnScrollListener(paginationListener)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RecipesGridView)
        spanCount = attributeArray.getInteger(R.styleable.RecipesGridView_spanCount, 1)
        spacing = attributeArray.getInteger(R.styleable.RecipesGridView_spacing, 30)
        includeEdge = attributeArray.getBoolean(R.styleable.RecipesGridView_includeEdge, false)
        attributeArray.recycle()
    }

    fun scrollToTop() {
        manager.scrollToPosition(0)
    }

    fun clear() {
        getListAdapter().clear()
        paginationListener.resetState()
    }

    fun render(recipes: List<RecipeViewModel>) {
        getListAdapter().setItems(recipes)
    }

    fun bind(
        eventsListener: ((Event) -> Unit)
    ) {
        this.eventsListener = eventsListener
        getListAdapter().setListener(eventsListener)
    }

    private fun getListAdapter(): RecipesAdapter {
        return adapter as RecipesAdapter
    }

    sealed class Event {
        data class ItemClick(val recipe: RecipeViewModel) : Event()
        data class ItemLongClick(val recipe: RecipeViewModel) : Event()
        object OnEndReached : Event()
    }
}
