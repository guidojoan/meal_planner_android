package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.astutify.mealplanner.AppConstants
import com.astutify.mealplanner.coreui.entity.ActivityResult
import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import com.astutify.mealplanner.coreui.entity.MeasurementViewModel
import com.astutify.mealplanner.ingredient.IngredientComponentProvider
import com.astutify.mealplanner.ingredient.databinding.ViewRecipeIngredientBinding
import com.astutify.mealplanner.ingredient.presentation.list.adapter.IngredientsListView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class RecipeIngredientActivity :
    AppCompatActivity(),
    RecipeIngredientsView,
    SearchView.OnQueryTextListener,
    AddRecipeIngredientDialog.AddRecipeIngredientDialogListener {

    @Inject
    lateinit var controller: RecipeIngredientsViewController

    private lateinit var view: ViewRecipeIngredientBinding

    private val eventsRelay: Relay<RecipeIngredientsView.Intent> = PublishRelay.create()

    override val events: Observable<RecipeIngredientsView.Intent>
        get() = eventsRelay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ViewRecipeIngredientBinding.inflate(layoutInflater)
        setContentView(view.root)

        val ingredientGroupId = intent.getStringExtra(IngredientGroupIdExtra)!!

        (application as IngredientComponentProvider)
            .ingredientComponent
            .recipeIngredientActivityBuilder()
            .withActivity(this)
            .ingredientGroupId(ingredientGroupId)
            .with(this)
            .build()
            .inject(this)

        bindViews()
        lifecycle.addObserver(controller)
    }

    private fun bindViews() {
        view.searchField.setOnQueryTextListener(this)
        view.searchField.onActionViewExpanded()
        view.toolbar.setNavigationOnClickListener {
            eventsRelay.accept(RecipeIngredientsView.Intent.ClickBack)
        }
        view.searchResults.bind {
            when (it) {
                is IngredientsListView.Event.IngredientClicked ->
                    eventsRelay.accept(
                        RecipeIngredientsView.Intent.IngredientClick(it.ingredient)
                    )
                IngredientsListView.Event.AddIngredientClicked -> {
                    eventsRelay.accept(RecipeIngredientsView.Intent.ClickAddIngredient)
                }
                IngredientsListView.Event.OnEndReached -> {
                    eventsRelay.accept(RecipeIngredientsView.Intent.EnOfListReached)
                }
            }
        }
    }

    override fun render(viewState: RecipeIngredientsViewState) {
        view.searchResults.render(viewState.ingredients)
        viewState.selectedIngredient?.let {
            val dialog = AddRecipeIngredientDialog.newInstance(it)
            dialog.show(supportFragmentManager, AddRecipeIngredientDialogTag)
        }
    }

    override fun onSelectRecipeIngredient(quantity: Float, measurement: MeasurementViewModel) {
        eventsRelay.accept(
            RecipeIngredientsView.Intent.IngredientQuantitySet(
                quantity,
                measurement
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EditIngredientResultCode && resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableExtra<ActivityResult<IngredientViewModel>>(AppConstants.ActivityResultObject)
                ?.let {
                    eventsRelay.accept(RecipeIngredientsView.Intent.IngredientClick(it.result))
                }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        eventsRelay.accept(
            RecipeIngredientsView.Intent.Search(
                newText!!
            )
        )
        return false
    }

    companion object {
        const val IngredientGroupIdExtra = "IngredientGroupIdExtra"
        const val EditIngredientResultCode = 8
        const val AddRecipeIngredientDialogTag = "addRecipeIngredientDialogTag"
    }

    @Subcomponent
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: RecipeIngredientsView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            @BindsInstance
            fun ingredientGroupId(@Named("ingredientGroupId") id: String): Builder

            fun build(): Component
        }

        fun inject(fragment: RecipeIngredientActivity)
    }
}
