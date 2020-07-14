package com.astutify.mealplanner.recipe.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.recipe.R
import com.astutify.mealplanner.recipe.RecipeComponentProvider
import com.astutify.mealplanner.recipe.RecipeOutNavigatorModule
import com.astutify.mealplanner.recipe.databinding.ViewDetailRecipeBinding
import com.astutify.mealplanner.recipe.presentation.detail.mvi.RecipeDetailViewState
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class RecipeDetailViewActivity : AppCompatActivity(), RecipeDetailView {

    @Inject
    lateinit var controller: RecipeDetailViewController
    private val eventsRelay: Relay<RecipeDetailView.Intent> = PublishRelay.create()
    override val events: Observable<RecipeDetailView.Intent>
        get() = eventsRelay
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var view: ViewDetailRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ViewDetailRecipeBinding.inflate(layoutInflater)
        setContentView(view.root)

        val recipe = intent.getParcelableExtra<RecipeViewModel>(RECIPE_EXTRA)!!

        (application as RecipeComponentProvider)
            .recipeComponent
            .recipeDetailViewActivityBuilder()
            .withActivity(this)
            .withRecipe(recipe)
            .with(this)
            .build()
            .inject(this)

        initView()
        lifecycle.addObserver(controller)
    }

    private fun initView() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.ingredients.card)
        view.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                R.color.transparent
            )
        )
        view.toolbar.setNavigationOnClickListener {
            eventsRelay.accept(RecipeDetailView.Intent.ClickBack)
        }
        view.ingredients.servings.setListener {
            eventsRelay.accept(RecipeDetailView.Intent.ServingsChanged(it))
        }
    }

    override fun render(viewState: RecipeDetailViewState) {
        view.collapsingToolbar.title = viewState.recipe.name
        view.ingredients.servings.setValue(viewState.recipe.servings)
        view.name.text = viewState.recipe.name
        view.directions.text = viewState.recipe.directions
        view.ingredients.ingredientsList.bind(viewState.recipe.ingredientGroups) {}
        loadImage(viewState.recipe.imageUrl)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(view.picture)
    }

    @Subcomponent(modules = [RecipeOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: RecipeDetailView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            @BindsInstance
            fun withRecipe(recipe: RecipeViewModel): Builder

            fun build(): Component
        }

        fun inject(activity: RecipeDetailViewActivity)
    }

    companion object {
        const val RECIPE_EXTRA = "recipeExtra"
        const val EditRecipeActivityResult = 8
    }
}
