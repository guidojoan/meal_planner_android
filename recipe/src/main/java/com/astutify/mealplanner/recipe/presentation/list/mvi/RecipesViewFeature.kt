package com.astutify.mealplanner.recipe.presentation.list.mvi

import com.astutify.mvi.Feature
import javax.inject.Inject

class RecipesViewFeature @Inject constructor(
    reducer: RecipesViewReducer,
    effectHandler: RecipesViewEffectHandler
) : Feature<RecipesViewState, RecipesViewEvent, RecipesViewEffect>(
    initialState = RecipesViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): RecipesViewEvent =
        RecipesViewEvent.CheckLoginStatus
}
