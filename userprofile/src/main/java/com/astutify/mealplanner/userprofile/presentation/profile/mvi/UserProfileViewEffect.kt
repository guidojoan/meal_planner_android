package com.astutify.mealplanner.userprofile.presentation.profile.mvi

sealed class UserProfileViewEffect {
    object LogOut : UserProfileViewEffect()
    object LeaveHouse : UserProfileViewEffect()
    object LoadData : UserProfileViewEffect()
    object ShowAbout : UserProfileViewEffect()
}
