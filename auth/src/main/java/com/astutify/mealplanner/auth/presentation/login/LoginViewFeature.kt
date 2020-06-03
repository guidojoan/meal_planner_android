package com.astutify.mealplanner.auth.presentation.login

import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import javax.inject.Inject

class LoginViewFeature @Inject constructor(
    reducer: LoginViewReducer,
    effectHandler: LoginViewEffectHandler
) : Feature<LoginViewState, LoginViewEvent, LoginViewEffect>(
    initialState = LoginViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): LoginViewEvent? = null
}
