package com.astutify.mealplanner.recipe.presentation.detail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.recipe.presentation.detail.mvi.RecipeDetailViewEvent
import com.astutify.mealplanner.recipe.presentation.detail.mvi.RecipeDetailViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecipeDetailViewController @Inject constructor(
    private val view: RecipeDetailView,
    private val feature: RecipeDetailViewFeature
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
                            RecipeDetailView.Intent.ClickBack -> feature.accept(
                                RecipeDetailViewEvent.ClickBack
                            )
                            is RecipeDetailView.Intent.ServingsChanged -> feature.accept(
                                RecipeDetailViewEvent.ServingsChanged(it.servings)
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
