package com.astutify.mealplanner.ingredient.presentation.list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewEvent
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IngredientsViewController @Inject constructor(
    private val view: IngredientsView,
    private val feature: IngredientsViewFeature
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
                            is IngredientsView.Intent.Search -> feature.accept(
                                IngredientsViewEvent.Search(
                                    it.name
                                )
                            )
                            is IngredientsView.Intent.ClickAddIngredient -> feature.accept(
                                IngredientsViewEvent.ClickAddIngredient
                            )
                            IngredientsView.Intent.ClickRetry -> feature.accept(IngredientsViewEvent.LoadData)
                            is IngredientsView.Intent.IngredientClicked -> feature.accept(
                                IngredientsViewEvent.IngredientClicked(it.ingredient)
                            )
                            is IngredientsView.Intent.IngredientAdded -> feature.accept(
                                IngredientsViewEvent.IngredientAdded(it.ingredient)
                            )
                            is IngredientsView.Intent.IngredientUpdated -> feature.accept(
                                IngredientsViewEvent.IngredientUpdated(it.ingredient)
                            )
                            IngredientsView.Intent.ClickRefresh -> feature.accept(
                                IngredientsViewEvent.ClickRefresh
                            )
                            IngredientsView.Intent.ClickCloseSearch -> feature.accept(
                                IngredientsViewEvent.ClickCloseSearch
                            )
                            IngredientsView.Intent.OnEndOfListReached -> feature.accept(
                                IngredientsViewEvent.EndOfListReached
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
    }
}
