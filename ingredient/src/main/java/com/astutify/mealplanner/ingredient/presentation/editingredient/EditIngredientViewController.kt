package com.astutify.mealplanner.ingredient.presentation.editingredient

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewEvent
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Mockable
class EditIngredientViewController @Inject constructor(
    private val view: EditIngredientView,
    private val feature: EditIngredientViewFeature
) : DefaultLifecycleObserver {

    private val disposable = CompositeDisposable()

    fun onSaveInstance(outState: Bundle) {
        feature.saveState(outState)
    }

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
                            is EditIngredientView.Intent.ClickSave -> feature.accept(
                                EditIngredientViewEvent.Save
                            )
                            is EditIngredientView.Intent.MeasurementSelected -> feature.accept(
                                EditIngredientViewEvent.MeasurementSelected(it.measurement)
                            )
                            is EditIngredientView.Intent.CategorySelected -> feature.accept(
                                EditIngredientViewEvent.CategorySelected(it.category)
                            )
                            is EditIngredientView.Intent.NameChanged -> feature.accept(
                                EditIngredientViewEvent.NameChanged(it.name)
                            )
                            is EditIngredientView.Intent.PackageAdded -> feature.accept(
                                EditIngredientViewEvent.PackageAdded(it.name, it.quantity)
                            )
                            is EditIngredientView.Intent.PackageRemoved -> feature.accept(
                                EditIngredientViewEvent.PackageRemoved(it.id)
                            )
                            EditIngredientView.Intent.ClickBack -> feature.accept(
                                EditIngredientViewEvent.ClickBack
                            )
                            EditIngredientView.Intent.ClickAddCustomMeasurement -> feature.accept(
                                EditIngredientViewEvent.ClickAddCustomMeasurement
                            )
                            is EditIngredientView.Intent.CustomMeasurementAdded -> feature.accept(
                                EditIngredientViewEvent.CustomMeasurementAdded(
                                    it.measurement,
                                    it.quantity
                                )
                            )
                            is EditIngredientView.Intent.CustomMeasurementRemoved -> feature.accept(
                                EditIngredientViewEvent.CustomMeasurementRemoved(it.measurement)
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
