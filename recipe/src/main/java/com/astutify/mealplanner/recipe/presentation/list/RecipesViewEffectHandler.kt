package com.astutify.mealplanner.recipe.presentation.list

import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.coreui.entity.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import com.astutify.mealplanner.recipe.Navigator
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.recipe.domain.GetRecipesNextPageUseCase
import com.astutify.mealplanner.recipe.domain.GetRecipesUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.just
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class RecipesViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val navigator: Navigator,
    private val outNavigatorComponent: RecipeOutNavigator,
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipesNextPageUseCase: GetRecipesNextPageUseCase,
    private val sessionManager: SessionManager
) : EffectHandler<RecipesViewState, RecipesViewEvent, RecipesViewEffect> {

    override fun invoke(
        state: RecipesViewState,
        effect: RecipesViewEffect
    ): Flowable<out RecipesViewEvent> {
        return when (effect) {
            is RecipesViewEffect.CheckLoginStatus -> {
                if (!sessionManager.isLogued()) {
                    outNavigatorComponent.goToLogin()
                    never<RecipesViewEvent>()
                } else if (!sessionManager.hasHouse()) {
                    outNavigatorComponent.goToHouseEdit()
                    never<RecipesViewEvent>()
                } else {
                    just(RecipesViewEvent.Load)
                }
            }
            is RecipesViewEffect.LoadData -> {
                getRecipesUseCase()
                    .toFlowable()
                    .map<RecipesViewEvent> {
                        RecipesViewEvent.DataLoaded(it.map { it.toPresentation() })
                    }
                    .onErrorReturn {
                        RecipesViewEvent.LoadingError
                    }
                    .observeOn(main)
                    .startWith(RecipesViewEvent.Loading)
            }
            is RecipesViewEffect.GoToAddRecipe -> {
                navigator.goToAddRecipe()
                never<RecipesViewEvent>()
            }
            is RecipesViewEffect.GoToRecipeDetail -> {
                navigator.goToRecipeDetail(effect.recipe)
                never<RecipesViewEvent>()
            }
            is RecipesViewEffect.GoToEditRecipe -> {
                navigator.goToEditRecipe(effect.recipe)
                never<RecipesViewEvent>()
            }
            is RecipesViewEffect.Search -> {
                if (effect.name.length > 2) {
                    getRecipesUseCase(effect.name)
                        .toFlowable()
                        .observeOn(main)
                        .map<RecipesViewEvent> {
                            RecipesViewEvent.DataLoaded(it.map { it.toPresentation() })
                        }
                        .onErrorReturn {
                            null
                        }
                } else {
                    never()
                }
            }
            RecipesViewEffect.EndOfListReached -> {
                getRecipesNextPageUseCase()
                    .toFlowable()
                    .map<RecipesViewEvent> {
                        RecipesViewEvent.NextDataLoaded(it.map { it.toPresentation() })
                    }
                    .onErrorReturn {
                        RecipesViewEvent.LoadingNextError
                    }
                    .observeOn(main)
                    .startWith(RecipesViewEvent.LoadingNext)
            }
        }
    }
}
