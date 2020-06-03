package com.astutify.mealplanner.userprofile.presentation.house

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import com.astutify.mealplanner.coreui.entity.HouseViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseEditViewState(
    val houses: List<HouseViewModel> = emptyList(),
    val houseName: String = String.EMPTY,
    val houseSelected: HouseViewModel? = null,
    val joinCode: Int = 0,
    val joinCodeVisible: Boolean = false,
    val continueEnabled: Boolean = false,
    val loading: Loading? = null,
    val error: Error? = null
) : Parcelable {

    fun copyState(
        houses: List<HouseViewModel> = this.houses,
        houseName: String = this.houseName,
        houseSelected: HouseViewModel? = this.houseSelected,
        joinCode: Int = this.joinCode,
        joinCodeVisible: Boolean = this.joinCodeVisible,
        continueEnabled: Boolean = this.continueEnabled,
        loading: Loading? = null,
        error: Error? = null
    ) = copy(
        houses = houses,
        houseName = houseName,
        houseSelected = houseSelected,
        joinCode = joinCode,
        joinCodeVisible = joinCodeVisible,
        continueEnabled = continueEnabled,
        loading = loading,
        error = error
    )

    enum class Loading {
        LOADING
    }

    enum class Error {
        NETWORK, JOIN_CODE
    }
}

sealed class HouseEditViewEvent {
    data class OnNameChanged(val name: String) : HouseEditViewEvent()
    data class OnJoinCodeChanged(val joinCode: Int) : HouseEditViewEvent()
    data class DataLoaded(val houses: List<HouseViewModel>) : HouseEditViewEvent()
    object ContinueClicked : HouseEditViewEvent()
    object LoadingError : HouseEditViewEvent()
    object JoinCodeError : HouseEditViewEvent()
    object Loading : HouseEditViewEvent()
}

sealed class HouseEditViewEffect {
    data class SearchHouse(val name: String) : HouseEditViewEffect()
    data class CreateHouse(val name: String) : HouseEditViewEffect()
    data class JoinHouse(val houseId: String, val joinCode: Int) : HouseEditViewEffect()
}
