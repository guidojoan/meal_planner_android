package com.astutify.mealplanner.ingredient.presentation.list.mvi

import com.astutify.mvi.Feature
import javax.inject.Inject

class IngredientsViewFeature @Inject constructor(
    reducer: IngredientsVewReducer,
    effectHandler: IngredientsViewEffectHandler
) : Feature<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>(
    initialState = IngredientsViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() =
        IngredientsViewEvent.LoadData
}
