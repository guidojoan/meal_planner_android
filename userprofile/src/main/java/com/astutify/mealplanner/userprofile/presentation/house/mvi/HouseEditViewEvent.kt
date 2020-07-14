package com.astutify.mealplanner.userprofile.presentation.house.mvi

import com.astutify.mealplanner.coreui.model.HouseViewModel

sealed class HouseEditViewEvent {
    data class OnNameChanged(val name: String) : HouseEditViewEvent()
    data class OnJoinCodeChanged(val joinCode: Int) : HouseEditViewEvent()
    data class DataLoaded(val houses: List<HouseViewModel>) : HouseEditViewEvent()
    object ContinueClicked : HouseEditViewEvent()
    object LoadingError : HouseEditViewEvent()
    object JoinCodeError : HouseEditViewEvent()
    object Loading : HouseEditViewEvent()
}