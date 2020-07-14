package com.astutify.mealplanner.recipe.presentation.list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewEvent
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecipesViewController @Inject constructor(
    private val view: RecipesView,
    private val feature: RecipesViewFeature
) : DefaultLifecycleObserver {

    private val disposable = CompositeDisposable()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        disposable.add(
            Observable.wrap(feature)
                .distinctUntilChanged()
                .subscribe(
                    view::render
                ) { throw RuntimeException(it) }
        )

        disposable.add(
            Observable.wrap(view.events)
                .subscribe(
                    {
                        when (it) {
                            is RecipesView.Intent.ClickAddRecipe -> feature.accept(
                                RecipesViewEvent.ClickAddRecipe
                            )
                            is RecipesView.Intent.ClickRetry -> feature.accept(
                                RecipesViewEvent.Load
                            )
                            is RecipesView.Intent.Search -> feature.accept(
                                RecipesViewEvent.Search(
                                    it.name
                                )
                            )
                            is RecipesView.Intent.RecipeClicked -> feature.accept(
                                RecipesViewEvent.RecipeClicked(
                                    it.recipe
                                )
                            )
                            is RecipesView.Intent.RecipeAdded -> feature.accept(
                                RecipesViewEvent.RecipeAdded(
                                    it.recipe
                                )
                            )
                            is RecipesView.Intent.RecipeLongClicked -> feature.accept(
                                RecipesViewEvent.RecipeLongClicked(it.recipe)
                            )
                            is RecipesView.Intent.RecipeUpdated -> feature.accept(
                                RecipesViewEvent.RecipeUpdated(
                                    it.recipe
                                )
                            )
                            RecipesView.Intent.ClickRefresh -> feature.accept(RecipesViewEvent.ClickRefresh)
                            RecipesView.Intent.SearchCancelClicked -> feature.accept(
                                RecipesViewEvent.ClickCloseSearch
                            )
                            is RecipesView.Intent.RecipeDeleted -> feature.accept(
                                RecipesViewEvent.RecipeDeleted(
                                    it.recipe
                                )
                            )
                            RecipesView.Intent.EndOfListReached -> feature.accept(RecipesViewEvent.EndOfListReached)
                        }
                    },
                    { throw RuntimeException(it) }
                )
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable.dispose()
    }
}
