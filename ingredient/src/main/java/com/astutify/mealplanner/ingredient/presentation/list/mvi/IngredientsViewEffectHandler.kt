package com.astutify.mealplanner.ingredient.presentation.list.mvi

import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mvi.EffectHandler
import com.astutify.mealplanner.ingredient.presentation.Navigator
import com.astutify.mealplanner.ingredient.domain.GetIngredientsNexPageUseCase
import com.astutify.mealplanner.ingredient.domain.GetIngredientsUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class IngredientsViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val navigator: Navigator,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val getIngredientsNexPageUseCase: GetIngredientsNexPageUseCase
) : EffectHandler<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect> {

    override fun invoke(
        state: IngredientsViewState,
        effect: IngredientsViewEffect
    ): Flowable<out IngredientsViewEvent> {
        return when (effect) {
            is IngredientsViewEffect.LoadData -> {
                getIngredientsUseCase()
                    .observeOn(main)
                    .toFlowable()
                    .map<IngredientsViewEvent> {
                        IngredientsViewEvent.DataLoaded(
                            it.map { it.toPresentation() })
                    }
                    .onErrorReturn {
                        IngredientsViewEvent.LoadingError
                    }
                    .startWith(IngredientsViewEvent.Loading)
            }
            is IngredientsViewEffect.Search -> {
                if (effect.name.length > 2) {
                    getIngredientsUseCase(effect.name)
                        .observeOn(main)
                        .toFlowable()
                        .map<IngredientsViewEvent> {
                            IngredientsViewEvent.DataLoaded(
                                it.map { it.toPresentation() }
                            )
                        }
                        .onErrorReturn {
                            null
                        }
                } else {
                    never<IngredientsViewEvent>()
                }
            }
            is IngredientsViewEffect.GoToAddIngredient -> {
                navigator.goToAddIngredient()
                never<IngredientsViewEvent>()
            }
            is IngredientsViewEffect.GoToEditIngredient -> {
                navigator.goToEditIngredient(effect.ingredient)
                never<IngredientsViewEvent>()
            }
            IngredientsViewEffect.LoadMoreData -> {
                getIngredientsNexPageUseCase()
                    .observeOn(main)
                    .toFlowable()
                    .map<IngredientsViewEvent> {
                        IngredientsViewEvent.NextDataLoaded(
                            it.map { it.toPresentation() })
                    }
                    .onErrorReturn {
                        IngredientsViewEvent.LoadingNextError
                    }
                    .startWith(IngredientsViewEvent.LoadingNext)
            }
        }
    }
}
