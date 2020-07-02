package com.astutify.mealplanner.ingredient.presentation.editingredient

import com.astutify.mealplanner.core.domain.GetIngredientCategoriesUseCase
import com.astutify.mealplanner.core.domain.GetMeasurementsUseCase
import com.astutify.mealplanner.core.entity.data.error.BadRequest
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.mapper.toDomain
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import com.astutify.mealplanner.ingredient.Navigator
import com.astutify.mealplanner.ingredient.domain.AddIngredientUseCase
import com.astutify.mealplanner.ingredient.domain.UpdateIngredientUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class EditIngredientViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val navigator: Navigator,
    private val addIngredientUseCase: AddIngredientUseCase,
    private val getIngredientCategoriesUseCase: GetIngredientCategoriesUseCase,
    private val getMeasurementsUseCase: GetMeasurementsUseCase,
    private val updateIngredientUseCase: UpdateIngredientUseCase
) : EffectHandler<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect> {

    override fun invoke(
        state: EditIngredientViewState,
        effect: EditIngredientViewEffect
    ): Flowable<out EditIngredientViewEvent> {
        return when (effect) {
            is EditIngredientViewEffect.LoadData -> {
                getIngredientCategoriesUseCase()
                    .observeOn(main)
                    .toFlowable()
                    .flatMap<EditIngredientViewEvent> { categories ->
                        getMeasurementsUseCase()
                            .observeOn(main)
                            .toFlowable()
                            .map { measurements ->
                                EditIngredientViewEvent.DataLoaded(
                                    measurements.filter { it.primary }.map { it.toPresentation() },
                                    measurements.filterNot { it.primary }.map { it.toPresentation() },
                                    categories.map { it.toPresentation() }
                                )
                            }
                    }
                    .onErrorReturn {
                        EditIngredientViewEvent.LoadingError
                    }
                    .startWith(EditIngredientViewEvent.Loading)
            }
            is EditIngredientViewEffect.Save -> {
                return when (state.mode) {
                    EditIngredientViewState.Mode.NEW -> saveIngredient(state.ingredient)
                    EditIngredientViewState.Mode.EDIT -> updateIngredient(state.ingredient)
                }
            }
            EditIngredientViewEffect.GoBack -> {
                navigator.goBack()
                never<EditIngredientViewEvent>()
            }
        }
    }

    private fun saveIngredient(ingredient: IngredientViewModel) =
        addIngredientUseCase(ingredient.toDomain())
            .observeOn(main)
            .toFlowable()
            .flatMap<EditIngredientViewEvent> { ingredient ->
                navigator.finishAddIngredient(
                    ActivityResult(
                        ingredient.toPresentation().copy(status = IngredientViewModel.Status.NEW)
                    )
                )
                never<EditIngredientViewEvent>()
            }
            .onErrorReturn {
                if (it is BadRequest && it.isNameTaken()) {
                    EditIngredientViewEvent.ErrorNameTaken
                } else {
                    EditIngredientViewEvent.ErrorSave
                }
            }
            .startWith(EditIngredientViewEvent.LoadingSave)

    private fun updateIngredient(ingredient: IngredientViewModel) =
        updateIngredientUseCase(ingredient.toDomain())
            .observeOn(main)
            .toFlowable()
            .flatMap<EditIngredientViewEvent> { ingredient ->
                navigator.finishAddIngredient(
                    ActivityResult(
                        ingredient.toPresentation().copy(status = IngredientViewModel.Status.UPDATED)
                    )
                )
                never<EditIngredientViewEvent>()
            }
            .onErrorReturn {
                if (it is BadRequest && it.isNameTaken()) {
                    EditIngredientViewEvent.ErrorNameTaken
                } else {
                    EditIngredientViewEvent.ErrorSave
                }
            }
            .startWith(EditIngredientViewEvent.LoadingSave)
}
