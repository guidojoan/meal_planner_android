package com.astutify.mealplanner.auth.presentation.login.mvi

import com.astutify.mealplanner.core.model.data.GoogleUser

sealed class LoginViewEffect {
    class Login(val googleUser: GoogleUser) : LoginViewEffect()
}
