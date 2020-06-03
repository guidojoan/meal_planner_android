package com.astutify.mealplanner.userprofile.presentation.profile

import io.reactivex.Observable

interface UserProfileView {

    fun render(viewState: UserProfileViewState)

    val events: Observable<Intent>

    sealed class Intent {
        object ClickRetry : Intent()
        object ClickLogout : Intent()
        object ClickLeaveHouse : Intent()
        object LongClickCard : Intent()
    }
}
