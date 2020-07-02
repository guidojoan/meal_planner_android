package com.astutify.mealplanner.userprofile.presentation.profile

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

sealed class UserProfileViewEvent {
    object LogOut : UserProfileViewEvent()
    object LeaveHouse : UserProfileViewEvent()
    object LoadData : UserProfileViewEvent()
    object Loading : UserProfileViewEvent()
    object LoadingLogOut : UserProfileViewEvent()
    object LoadingLeaveHouse : UserProfileViewEvent()
    data class DataLoaded(val user: UserViewModel, val house: HouseViewModel) :
        UserProfileViewEvent()

    object LoadingError : UserProfileViewEvent()
    object LogOutError : UserProfileViewEvent()
    object LeaveHouseError : UserProfileViewEvent()
    object ClickCard : UserProfileViewEvent()
    data class ShowAbout(val about: AboutViewModel) : UserProfileViewEvent()
}

sealed class UserProfileViewEffect {
    object LogOut : UserProfileViewEffect()
    object LeaveHouse : UserProfileViewEffect()
    object LoadData : UserProfileViewEffect()
    object ShowAbout : UserProfileViewEffect()
}
