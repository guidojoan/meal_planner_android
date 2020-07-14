package com.astutify.mealplanner.userprofile.presentation.profile.mvi

import android.os.Parcelable
import com.astutify.mealplanner.coreui.model.AboutViewModel
import com.astutify.mealplanner.coreui.model.HouseViewModel
import com.astutify.mealplanner.coreui.model.UserViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileViewState(
    val houses: HouseViewModel? = null,
    val user: UserViewModel? = null,
    val about: AboutViewModel? = null,
    val loading: Loading? = null,
    val error: Error? = null
) : Parcelable {

    fun copyState(
        houses: HouseViewModel? = this.houses,
        user: UserViewModel? = this.user,
        about: AboutViewModel? = null,
        loading: Loading? = null,
        error: Error? = null
    ) = copy(houses = houses, user = user, about = about, loading = loading, error = error)

    enum class Loading {
        LOADING, LOADING_LOGOUT, LOADING_LEAVE_HOUSE
    }

    enum class Error {
        LOADING_ERROR, LOGOUT_ERROR, LEAVE_HOUSE_ERROR
    }

    enum class Dialog {
        ABOUT
    }
}