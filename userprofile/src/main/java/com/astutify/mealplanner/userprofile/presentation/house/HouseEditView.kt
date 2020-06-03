package com.astutify.mealplanner.userprofile.presentation.house

import io.reactivex.Observable

interface HouseEditView {

    fun render(viewState: HouseEditViewState)

    val events: Observable<Intent>

    sealed class Intent {
        data class OnNameChanged(val name: String) : Intent()
        data class OnJoinCodeChanged(val joinCode: Int) : Intent()
        object OnContinueClicked : Intent()
    }
}
