package com.astutify.mealplanner.userprofile.presentation.house.mvi

import com.astutify.mealplanner.coreui.model.HouseViewModel
import com.astutify.mvi.Effect
import com.astutify.mvi.Next
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class HouseEditViewReducer @Inject constructor() :
    Reducer<HouseEditViewState, HouseEditViewEvent, HouseEditViewEffect>() {

    override fun invoke(
        state: HouseEditViewState,
        event: HouseEditViewEvent
    ): ReducerResult<HouseEditViewState, HouseEditViewEffect> {
        return when (event) {
            is HouseEditViewEvent.OnNameChanged -> {
                val houseSelected = getHouseFromList(event.name, state.houses)
                return if (houseSelected != null) {
                    State(
                        state.copyState(
                            houseSelected = houseSelected,
                            continueEnabled = true,
                            joinCodeVisible = false
                        )
                    )
                } else {
                    Next(
                        state = state.copyState(
                            houseName = event.name,
                            joinCode = 0,
                            joinCodeVisible = false
                        ),
                        effect = HouseEditViewEffect.SearchHouse(
                            event.name
                        )
                    )
                }
            }
            is HouseEditViewEvent.ContinueClicked -> {
                return when {
                    state.houseSelected != null && state.joinCode != 0 -> Effect(
                        HouseEditViewEffect.JoinHouse(
                            state.houseSelected.id,
                            state.joinCode
                        )
                    )
                    state.houseSelected != null -> State(
                        state.copyState(joinCodeVisible = true, continueEnabled = false)
                    )
                    else -> Effect(
                        HouseEditViewEffect.CreateHouse(
                            state.houseName
                        )
                    )
                }
            }
            is HouseEditViewEvent.DataLoaded -> {
                State(
                    addSearchResult(state, event.houses)
                )
            }
            HouseEditViewEvent.LoadingError -> {
                State(
                    state.copyState(
                        error = HouseEditViewState.Error.NETWORK
                    )
                )
            }
            HouseEditViewEvent.Loading -> {
                State(
                    state.copyState(
                        loading = HouseEditViewState.Loading.LOADING
                    )
                )
            }
            is HouseEditViewEvent.OnJoinCodeChanged -> {
                State(
                    state.copyState(
                        joinCode = event.joinCode,
                        continueEnabled = event.joinCode.toString().length == JOIN_CODE_LENGTH
                    )
                )
            }
            HouseEditViewEvent.JoinCodeError -> {
                State(
                    state.copyState(
                        error = HouseEditViewState.Error.JOIN_CODE
                    )
                )
            }
        }
    }

    private fun addSearchResult(state: HouseEditViewState, searchResults: List<HouseViewModel>) =
        state.copyState(
            houses = searchResults,
            continueEnabled = continueEnabled(state.houseName, searchResults),
            houseSelected = null
        )

    private fun continueEnabled(houseName: String, searchResults: List<HouseViewModel>) =
        when {
            houseName.isNotEmpty() && houseName.length >= HOUSE_NAME_MIN_LENGTH && !houseNameExists(
                houseName,
                searchResults
            ) -> true
            else -> false
        }

    private fun houseNameExists(houseName: String, houses: List<HouseViewModel>) =
        houses.find { it.name.equals(houseName, ignoreCase = true) }?.let { true } ?: false

    private fun getHouseFromList(houseName: String, houses: List<HouseViewModel>) =
        houses.find { it.name.equals(houseName, ignoreCase = true) }

    companion object {
        const val HOUSE_NAME_MIN_LENGTH = 8
        const val JOIN_CODE_LENGTH = 6
    }
}
