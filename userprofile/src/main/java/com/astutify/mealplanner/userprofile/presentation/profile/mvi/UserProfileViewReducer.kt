package com.astutify.mealplanner.userprofile.presentation.profile.mvi

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import javax.inject.Inject

class UserProfileViewReducer @Inject constructor() :
    Reducer<UserProfileViewState, UserProfileViewEvent, UserProfileViewEffect>() {

    override fun invoke(
        state: UserProfileViewState,
        event: UserProfileViewEvent
    ): ReducerResult<UserProfileViewState, UserProfileViewEffect> {
        return when (event) {
            UserProfileViewEvent.LogOut -> {
                Effect(
                    UserProfileViewEffect.LogOut
                )
            }
            UserProfileViewEvent.LeaveHouse -> {
                Effect(
                    UserProfileViewEffect.LeaveHouse
                )
            }
            UserProfileViewEvent.LoadData -> {
                Effect(
                    UserProfileViewEffect.LoadData
                )
            }
            UserProfileViewEvent.Loading -> {
                State(
                    state.copyState(
                        loading = UserProfileViewState.Loading.LOADING
                    )
                )
            }
            UserProfileViewEvent.LoadingLogOut -> {
                State(
                    state.copyState(
                        loading = UserProfileViewState.Loading.LOADING_LOGOUT
                    )
                )
            }
            UserProfileViewEvent.LoadingLeaveHouse -> {
                State(
                    state.copyState(
                        loading = UserProfileViewState.Loading.LOADING_LEAVE_HOUSE
                    )
                )
            }
            is UserProfileViewEvent.DataLoaded -> {
                State(
                    state.copyState(
                        user = event.user,
                        houses = event.house
                    )
                )
            }
            UserProfileViewEvent.LoadingError -> {
                State(
                    state.copyState(
                        error = UserProfileViewState.Error.LOADING_ERROR
                    )
                )
            }
            UserProfileViewEvent.LogOutError -> {
                State(
                    state.copyState(
                        error = UserProfileViewState.Error.LOGOUT_ERROR
                    )
                )
            }
            UserProfileViewEvent.LeaveHouseError -> {
                State(
                    state.copyState(
                        error = UserProfileViewState.Error.LEAVE_HOUSE_ERROR
                    )
                )
            }
            UserProfileViewEvent.ClickCard -> {
                Effect(UserProfileViewEffect.ShowAbout)
            }
            is UserProfileViewEvent.ShowAbout -> {
                State(state.copy(about = event.about))
            }
        }
    }
}
