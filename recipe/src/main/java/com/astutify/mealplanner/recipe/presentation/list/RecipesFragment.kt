package com.astutify.mealplanner.recipe.presentation.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.astutify.mealplanner.AppConstants
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.recipe.R
import com.astutify.mealplanner.recipe.RecipeComponentProvider
import com.astutify.mealplanner.recipe.RecipeOutNavigatorModule
import com.astutify.mealplanner.recipe.databinding.ViewListRecipesBinding
import com.astutify.mealplanner.recipe.presentation.list.adapter.RecipesGridView
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewState
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class RecipesFragment :
    Fragment(),
    RecipesView,
    SearchView.OnQueryTextListener {

    @Inject
    lateinit var controller: RecipesViewController

    override val events: Observable<RecipesView.Intent>
        get() = eventsRelay

    private val eventsRelay: Relay<RecipesView.Intent> = PublishRelay.create()

    private lateinit var view: ViewListRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = ViewListRecipesBinding.inflate(layoutInflater)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity!!.application as RecipeComponentProvider)
            .recipeComponent
            .homeFragmentBuilder()
            .withActivity(activity!! as AppCompatActivity)
            .with(this)
            .build()
            .inject(this)

        initToolbar()
        initViews()
        initSearchView()

        lifecycle.addObserver(controller)
    }

    private fun initViews() {
        view.swipeToRefresh.setOnRefreshListener {
            view.recipesList.clear()
            eventsRelay.accept(RecipesView.Intent.ClickRefresh)
        }
        view.buttonAddRecipe.setOnClickListener {
            eventsRelay.accept(RecipesView.Intent.ClickAddRecipe)
        }
        view.messageView.setListener {
            eventsRelay.accept(RecipesView.Intent.ClickRetry)
        }
        view.recipesList.bind {
            when (it) {
                is RecipesGridView.Event.ItemClick -> eventsRelay.accept(
                    RecipesView.Intent.RecipeClicked(
                        it.recipe
                    )
                )
                is RecipesGridView.Event.ItemLongClick -> eventsRelay.accept(
                    RecipesView.Intent.RecipeLongClicked(
                        it.recipe
                    )
                )
                RecipesGridView.Event.OnEndReached -> eventsRelay.accept(RecipesView.Intent.EndOfListReached)
            }
        }
    }

    private fun initToolbar() {
        view.toolbar.inflateMenu(R.menu.menu_recipes)
        view.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    private fun initSearchView() {
        val searchItem: MenuItem = view.toolbar.menu.findItem(R.id.searchBar)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnCloseListener {
            view.recipesList.clear()
            eventsRelay.accept(RecipesView.Intent.SearchCancelClicked)
            false
        }
        searchView.queryHint = getString(R.string.search_recipe)
        searchView.setOnQueryTextListener(this)
    }

    override fun render(viewState: RecipesViewState) {
        renderLoading(viewState)
        renderError(viewState)
        view.recipesList.render(viewState.recipes)
    }

    private fun renderLoading(viewState: RecipesViewState) {
        view.swipeToRefresh.isRefreshing = false
        when (viewState.loading) {
            RecipesViewState.Loading.LOADING -> view.loadingView.renderLoading()
            else -> view.loadingView.hide()
        }
    }

    private fun renderError(viewState: RecipesViewState) {
        when (viewState.error) {
            RecipesViewState.Error.LOADING_ERROR -> view.messageView.renderNetWorkError()
            RecipesViewState.Error.NO_RESULTS -> renderEmptyState()
            else -> view.messageView.hide()
        }
    }

    private fun renderEmptyState() {
        view.messageView.renderFullScreenMessage(
            getString(R.string.empty_state_recipes),
            ActivityCompat.getDrawable(
                context!!,
                R.drawable.img_pizza
            )!!
        )
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        eventsRelay.accept(
            RecipesView.Intent.Search(
                newText
            )
        )
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableExtra<ActivityResult<RecipeViewModel>>(AppConstants.ActivityResultObject)
                ?.let {
                    when (it.result.status) {
                        RecipeViewModel.Status.NEW -> {
                            eventsRelay.accept(RecipesView.Intent.RecipeAdded(it.result))
                            view.recipesList.scrollToTop()
                        }
                        RecipeViewModel.Status.UPDATED -> {
                            eventsRelay.accept(RecipesView.Intent.RecipeUpdated(it.result))
                        }
                        RecipeViewModel.Status.DELETED -> {
                            eventsRelay.accept(RecipesView.Intent.RecipeDeleted(it.result))
                        }
                        else -> {
                        }
                    }
                }
        }
    }

    @Subcomponent(modules = [RecipeOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: RecipesView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            fun build(): Component
        }

        fun inject(activity: RecipesFragment)
    }
}
