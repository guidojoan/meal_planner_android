package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.core.Mockable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Mockable
class RecipeIngredientsViewController @Inject constructor(
    private val view: RecipeIngredientsView,
    private val feature: RecipeIngredientsViewFeature
) : DefaultLifecycleObserver {

    private val disposable = CompositeDisposable()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        disposable.add(
            Observable.wrap(feature)
                .subscribe(
                    view::render
                ) { throw RuntimeException(it) }
        )

        disposable.add(
            Observable.wrap(view.events)
                .subscribe(
                    {
                        when (it) {
                            is RecipeIngredientsView.Intent.Search -> feature.accept(
                                RecipeIngredientsViewEvent.Search(it.name)
                            )
                            is RecipeIngredientsView.Intent.IngredientClick -> feature.accept(
                                RecipeIngredientsViewEvent.IngredientClick(it.ingredient)
                            )
                            is RecipeIngredientsView.Intent.IngredientQuantitySet -> feature.accept(
                                RecipeIngredientsViewEvent.IngredientQuantitySet(
                                    it.quantity,
                                    it.measurement
                                )
                            )
                            RecipeIngredientsView.Intent.ClickBack -> feature.accept(
                                RecipeIngredientsViewEvent.ClickBack
                            )
                            RecipeIngredientsView.Intent.ClickAddIngredient -> feature.accept(
                                RecipeIngredientsViewEvent.ClickAddIngredient
                            )
                            RecipeIngredientsView.Intent.EnOfListReached -> feature.accept(
                                RecipeIngredientsViewEvent.EndOfListReached
                            )
                        }
                    },
                    { throw RuntimeException(it) }
                )
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable.dispose()
        feature.dispose()
    }
}
