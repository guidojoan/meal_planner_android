package com.astutify.mealplanner.auth.presentation.login

import com.astutify.mealplanner.core.entity.data.GoogleUser
import io.reactivex.Observable

interface LoginView {

    fun render(viewState: LoginViewState)

    val events: Observable<Intent>

    sealed class Intent {
        data class Login(val googleUser: GoogleUser) : Intent()
        object GoogleSignInError : Intent()
    }
}
