package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import com.astutify.mealplanner.ingredient.Navigator
import com.astutify.mealplanner.ingredient.domain.GetIngredientsNexPageUseCase
import com.astutify.mealplanner.ingredient.domain.GetIngredientsUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class RecipeIngredientsViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val navigator: Navigator,
    private val getIngredientsFirstPageUseCase: GetIngredientsUseCase,
    private val getIngredientsNexPageUseCase: GetIngredientsNexPageUseCase,
    @Named("ingredientGroupId") private val ingredientGroupId: String
) : EffectHandler<RecipeIngredientsViewState, RecipeIngredientsViewEvent, RecipeIngredientsViewEffect> {

    override fun invoke(
        state: RecipeIngredientsViewState,
        effect: RecipeIngredientsViewEffect
    ): Flowable<out RecipeIngredientsViewEvent> {
        return when (effect) {
            is RecipeIngredientsViewEffect.Search -> {
                if (effect.name.length > 2) {
                    getIngredientsFirstPageUseCase(effect.name)
                        .observeOn(main)
                        .toFlowable()
                        .map<RecipeIngredientsViewEvent> {
                            RecipeIngredientsViewEvent.DataLoaded(
                                it.map { it.toPresentation() }
                            )
                        }
                        .onErrorReturn {
                            RecipeIngredientsViewEvent.LoadingError
                        }
                        .startWith(RecipeIngredientsViewEvent.Loading)
                } else {
                    never<RecipeIngredientsViewEvent>()
                }
            }
            is RecipeIngredientsViewEffect.IngredientQuantitySet -> {
                navigator.finishAddRecipeIngredient(
                    ActivityResult(
                        RecipeIngredientViewModel(
                            ingredient = effect.ingredient,
                            quantity = effect.quantity,
                            measurement = effect.measurement
                        ),
                        ingredientGroupId
                    )
                )
                never<RecipeIngredientsViewEvent>()
            }
            RecipeIngredientsViewEffect.ClickBack -> {
                navigator.goBack()
                never<RecipeIngredientsViewEvent>()
            }
            RecipeIngredientsViewEffect.GoToAddIngredient -> {
                navigator.goToAddIngredient()
                never<RecipeIngredientsViewEvent>()
            }
            RecipeIngredientsViewEffect.LoadMoreData -> {
                getIngredientsNexPageUseCase()
                    .observeOn(main)
                    .toFlowable()
                    .map<RecipeIngredientsViewEvent> {
                        RecipeIngredientsViewEvent.NextDataLoaded(
                            it.map { it.toPresentation() }
                        )
                    }
                    .onErrorReturn {
                        RecipeIngredientsViewEvent.LoadingNextError
                    }
                    .startWith(RecipeIngredientsViewEvent.LoadingNext)
            }
        }
    }
}
