package com.astutify.mealplanner.auth.presentation.login.mvi

import android.os.Parcelable
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