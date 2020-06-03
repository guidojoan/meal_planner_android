package com.astutify.mealplanner.ingredient.presentation.list

import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import javax.inject.Inject

class IngredientsViewFeature @Inject constructor(
    reducer: IngredientsVewReducer,
    effectHandler: IngredientsViewEffectHandler
) : Feature<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>(
    initialState = IngredientsViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() = IngredientsViewEvent.LoadData
}
