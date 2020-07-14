package com.astutify.mealplanner.recipe.presentation.detail.mvi

import com.astutify.mvi.EffectHandler
import com.astutify.mealplanner.recipe.presentation.Navigator
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import javax.inject.Inject

class RecipeDetailViewEffectHandler @Inject constructor(
    private val navigator: Navigator
) : EffectHandler<RecipeDetailViewState, RecipeDetailViewEvent, RecipeDetailViewEffect> {

    override fun invoke(
        state: RecipeDetailViewState,
        effect: RecipeDetailViewEffect
    ): Flowable<out RecipeDetailViewEvent> {
        return when (effect) {
            RecipeDetailViewEffect.GoBack -> {
                navigator.goBack()
                never()
            }
        }
    }
}
