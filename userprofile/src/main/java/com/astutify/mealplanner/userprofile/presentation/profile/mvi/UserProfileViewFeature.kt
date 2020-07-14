package com.astutify.mealplanner.userprofile.presentation.profile.mvi

import com.astutify.mvi.Feature
import javax.inject.Inject

class UserProfileViewFeature @Inject constructor(
    reducer: UserProfileViewReducer,
    effectHandler: UserProfileViewEffectHandler
) : Feature<UserProfileViewState, UserProfileViewEvent, UserProfileViewEffect>(
    initialState = UserProfileViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() =
        UserProfileViewEvent.LoadData
}
