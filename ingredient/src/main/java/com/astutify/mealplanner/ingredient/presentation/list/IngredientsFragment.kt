package com.astutify.mealplanner.ingredient.presentation.list

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
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.ingredient.IngredientComponentProvider
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.databinding.ViewListIngredientsBinding
import com.astutify.mealplanner.ingredient.presentation.list.adapter.IngredientsListView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class IngredientsFragment :
    Fragment(),
    IngredientsView,
    SearchView.OnQueryTextListener {

    @Inject
    lateinit var controller: IngredientsViewController

    @Inject
    lateinit var sessionManager: SessionManager

    private val eventsRelay: Relay<IngredientsView.Intent> = PublishRelay.create()

    override val events: Observable<IngredientsView.Intent>
        get() = eventsRelay

    private lateinit var view: ViewListIngredientsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = ViewListIngredientsBinding.inflate(layoutInflater)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as IngredientComponentProvider)
            .ingredientComponent
            .ingredientsFragmentBuilder()
            .withActivity(requireActivity() as AppCompatActivity)
            .with(this)
            .build()
            .inject(this)

        lifecycle.addObserver(controller)

        initToolbar()
        initSearchView()
        initViews()
    }

    private fun initToolbar() {
        view.toolbar.inflateMenu(R.menu.menu_ingredients)
        view.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    private fun initViews() {
        view.swipeToRefresh.setOnRefreshListener {
            view.ingredients.clear()
            eventsRelay.accept(IngredientsView.Intent.ClickRefresh)
        }
        view.buttonAddIngredient.setOnClickListener {
            eventsRelay.accept(IngredientsView.Intent.ClickAddIngredient)
        }
        view.messageView.setListener {
            eventsRelay.accept(IngredientsView.Intent.ClickRetry)
        }
        view.ingredients.bind {
            when (it) {
                is IngredientsListView.Event.IngredientClicked -> eventsRelay.accept(
                    IngredientsView.Intent.IngredientClicked(
                        it.ingredient
                    )
                )
                is IngredientsListView.Event.OnEndReached -> eventsRelay.accept(IngredientsView.Intent.OnEndOfListReached)
            }
        }
    }

    private fun initSearchView() {
        val searchItem: MenuItem = view.toolbar.menu.findItem(R.id.searchBar)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_ingredient)
        searchView.setOnCloseListener {
            view.ingredients.clear()
            eventsRelay.accept(IngredientsView.Intent.ClickCloseSearch)
            false
        }
        searchView.setOnQueryTextListener(this)
    }

    override fun render(viewState: IngredientsViewState) {
        view.ingredients.render(viewState.ingredients)
        renderLoading(viewState)
        renderError(viewState)
    }

    private fun renderLoading(viewState: IngredientsViewState) {
        view.swipeToRefresh.isRefreshing = false
        when (viewState.loading) {
            IngredientsViewState.Loading.LOADING -> view.loadingView.renderLoading()
            else -> view.loadingView.hide()
        }
    }

    private fun renderError(viewState: IngredientsViewState) {
        when (viewState.error) {
            IngredientsViewState.Error.LOADING_ERROR -> view.messageView.renderNetWorkError()
            IngredientsViewState.Error.NO_RESULTS -> renderEmptyState()
            else -> view.messageView.hide()
        }
    }

    private fun renderEmptyState() {
        view.messageView.renderFullScreenMessage(
            getString(R.string.empty_state_ingredients),
            ActivityCompat.getDrawable(
                requireContext(),
                R.drawable.img_huevo
            )!!
        )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        eventsRelay.accept(
            IngredientsView.Intent.Search(
                newText!!
            )
        )
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableExtra<ActivityResult<IngredientViewModel>>(AppConstants.ActivityResultObject)
                ?.let {
                    when (it.result.status) {
                        IngredientViewModel.Status.NEW -> {
                            eventsRelay.accept(IngredientsView.Intent.IngredientAdded(it.result))
                            view.ingredients.scrollToTop()
                        }
                        IngredientViewModel.Status.UPDATED -> {
                            eventsRelay.accept(IngredientsView.Intent.IngredientUpdated(it.result))
                        }
                        else -> {
                        }
                    }
                }
        }
    }

    @Subcomponent
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: IngredientsView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            fun build(): Component
        }

        fun inject(fragment: IngredientsFragment)
    }
}
