package com.astutify.mealplanner.recipe.presentation.edit

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EditRecipeViewController @Inject constructor(
    private val view: EditRecipeView,
    private val feature: EditRecipeViewFeature
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
                            is EditRecipeView.Intent.NameChanged -> feature.accept(
                                EditRecipeViewEvent.NameChanged(it.name)
                            )
                            is EditRecipeView.Intent.ServingsChanged -> feature.accept(
                                EditRecipeViewEvent.ServingsChanged(it.servings)
                            )
                            is EditRecipeView.Intent.IngredientAdded -> feature.accept(
                                EditRecipeViewEvent.IngredientAdded(
                                    it.recipeIngredient,
                                    it.ingredientGroupId
                                )
                            )
                            is EditRecipeView.Intent.DirectionsChanged -> feature.accept(
                                EditRecipeViewEvent.DirectionsChanged(it.directions)
                            )
                            EditRecipeView.Intent.SaveClick -> feature.accept(EditRecipeViewEvent.ClickSave)
                            is EditRecipeView.Intent.NewIngredientClicked -> feature.accept(
                                EditRecipeViewEvent.NewIngredientClicked(it.ingredientGroupId)
                            )
                            is EditRecipeView.Intent.IngredientGroupAdded -> feature.accept(
                                EditRecipeViewEvent.IngredientGroupAdded(it.name)
                            )
                            is EditRecipeView.Intent.IngredientGroupRemoveClicked -> feature.accept(
                                EditRecipeViewEvent.IngredientGroupRemoveClick(it.ingredientGroupId)
                            )
                            is EditRecipeView.Intent.IngredientRemoveClicked -> feature.accept(
                                EditRecipeViewEvent.RecipeIngredientRemoveClick(it.recipeIngredientId)
                            )
                            is EditRecipeView.Intent.ImagePicked -> feature.accept(
                                EditRecipeViewEvent.ImagePicked(it.imageUri)
                            )
                            is EditRecipeView.Intent.CategorySelected -> feature.accept(
                                EditRecipeViewEvent.CategorySelected(it.recipeCategory)
                            )
                            EditRecipeView.Intent.ClickBack -> feature.accept(EditRecipeViewEvent.ClickBack)
                            EditRecipeView.Intent.DeleteClicked -> feature.accept(
                                EditRecipeViewEvent.ClickDelete
                            )
                            EditRecipeView.Intent.ConfirmDelete -> feature.accept(
                                EditRecipeViewEvent.ConfirmDelete
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
