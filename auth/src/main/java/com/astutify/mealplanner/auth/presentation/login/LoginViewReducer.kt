package com.astutify.mealplanner.auth.presentation.login

import com.astutify.mealplanner.coreui.presentation.mvi.Effect
import com.astutify.mealplanner.coreui.presentation.mvi.Reducer
import com.astutify.mealplanner.coreui.presentation.mvi.ReducerResult
import com.astutify.mealplanner.coreui.presentation.mvi.State
import javax.inject.Inject

class LoginViewReducer @Inject constructor() : Reducer<LoginViewState, LoginViewEvent, LoginViewEffect>() {

    override fun invoke(
        state: LoginViewState,
        event: LoginViewEvent
    ): ReducerResult<LoginViewState, LoginViewEffect> {
        return when (event) {
            is LoginViewEvent.Loading -> State(
                state.copyState(
                    loading = true
                )
            )
            is LoginViewEvent.LoginBackend -> Effect(
                LoginViewEffect.Login(
                    event.googleUser
                )
            )
            is LoginViewEvent.LoginError -> State(
                state.copyState(
                    error = LoginViewState.Error.LOGIN
                )
            )
            is LoginViewEvent.NetworkConnectionError -> State(
                state.copyState(
                    error = LoginViewState.Error.NETWORK_CONNECTION
                )
            )
        }
    }
}
