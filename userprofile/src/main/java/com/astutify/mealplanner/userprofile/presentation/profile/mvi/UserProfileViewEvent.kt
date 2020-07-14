package com.astutify.mealplanner.userprofile.presentation.profile.mvi

import com.astutify.mealplanner.coreui.model.AboutViewModel
import com.astutify.mealplanner.coreui.model.HouseViewModel
import com.astutify.mealplanner.coreui.model.UserViewModel

sealed class UserProfileViewEvent {
    object LogOut : UserProfileViewEvent()
    object LeaveHouse : UserProfileViewEvent()
    object LoadData : UserProfileViewEvent()
    object Loading : UserProfileViewEvent()
    object LoadingLogOut : UserProfileViewEvent()
    object LoadingLeaveHouse : UserProfileViewEvent()
    data class DataLoaded(val user: UserViewModel, val house: HouseViewModel) : UserProfileViewEvent()
    object LoadingError : UserProfileViewEvent()
    object LogOutError : UserProfileViewEvent()
    object LeaveHouseError : UserProfileViewEvent()
    object ClickCard : UserProfileViewEvent()
    data class ShowAbout(val about: AboutViewModel) : UserProfileViewEvent()
}