package com.astutify.mealplanner.auth.presentation.login.mvi

import com.astutify.mealplanner.core.model.data.GoogleUser

sealed class LoginViewEvent {
    object Loading : LoginViewEvent()
    class LoginBackend(val googleUser: GoogleUser) : LoginViewEvent()
    object LoginError : LoginViewEvent()
    object NetworkConnectionError : LoginViewEvent()
}