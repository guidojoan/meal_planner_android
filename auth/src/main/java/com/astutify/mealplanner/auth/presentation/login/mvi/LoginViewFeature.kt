package com.astutify.mealplanner.auth.presentation.login.mvi

import com.astutify.mealplanner.auth.presentation.login.LoginViewEffectHandler
import com.astutify.mvi.Feature
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
