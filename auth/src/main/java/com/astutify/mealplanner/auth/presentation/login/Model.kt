package com.astutify.mealplanner.auth.presentation.login

import android.os.Parcelable
import com.astutify.mealplanner.core.model.data.GoogleUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginViewState(
    val loading: Boolean = false,
    val error: Error? = null
) : Parcelable {
    fun copyState(
        loading: Boolean = false,
        error: Error? = null
    ) = copy(loading = loading, error = error)

    enum class Error {
        LOGIN, NETWORK_CONNECTION
    }
}

sealed class LoginViewEvent {
    object Loading : LoginViewEvent()
    class LoginBackend(val googleUser: GoogleUser) : LoginViewEvent()
    object LoginError : LoginViewEvent()
    object NetworkConnectionError : LoginViewEvent()
}

sealed class LoginViewEffect {
    class Login(val googleUser: GoogleUser) : LoginViewEffect()
}
