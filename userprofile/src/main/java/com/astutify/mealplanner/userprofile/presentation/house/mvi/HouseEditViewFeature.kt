package com.astutify.mealplanner.userprofile.presentation.house.mvi

import com.astutify.mvi.Feature
import javax.inject.Inject

class HouseEditViewFeature @Inject constructor(
    reducer: HouseEditViewReducer,
    effectHandler: HouseEditEffectHandler
) : Feature<HouseEditViewState, HouseEditViewEvent, HouseEditViewEffect>(
    initialState = HouseEditViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): HouseEditViewEvent? = null
}
