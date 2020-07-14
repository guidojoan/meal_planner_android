package com.astutify.mealplanner.userprofile.presentation.profile.mvi

import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mvi.EffectHandler
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import com.astutify.mealplanner.userprofile.domain.GetAboutUseCase
import com.astutify.mealplanner.userprofile.domain.GetHouseUseCase
import com.astutify.mealplanner.userprofile.domain.GetUserProfileUseCase
import com.astutify.mealplanner.userprofile.domain.LeaveHouseUseCase
import com.astutify.mealplanner.userprofile.domain.LogoutUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class UserProfileViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val userProfileOutNavigator: UserProfileOutNavigator,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getHouseUseCase: GetHouseUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val leaveHouseUseCase: LeaveHouseUseCase,
    private val getAboutUseCase: GetAboutUseCase
) : EffectHandler<UserProfileViewState, UserProfileViewEvent, UserProfileViewEffect> {

    override fun invoke(
        state: UserProfileViewState,
        effect: UserProfileViewEffect
    ): Flowable<out UserProfileViewEvent> {
        return when (effect) {
            UserProfileViewEffect.LogOut -> {
                logoutUseCase()
                    .toFlowable()
                    .flatMap<UserProfileViewEvent> {
                        userProfileOutNavigator.goToHome()
                        never()
                    }
                    .onErrorReturn {
                        UserProfileViewEvent.LogOutError
                    }
                    .observeOn(main)
                    .startWith(UserProfileViewEvent.LoadingLogOut)
            }
            UserProfileViewEffect.LoadData -> {
                getUserProfileUseCase()
                    .toFlowable()
                    .flatMap<UserProfileViewEvent> { user ->
                        getHouseUseCase()
                            .toFlowable()
                            .map { house ->
                                UserProfileViewEvent.DataLoaded(
                                    user.toPresentation(),
                                    house.toPresentation()
                                )
                            }
                    }
                    .observeOn(main)
                    .onErrorReturn {
                        UserProfileViewEvent.LoadingError
                    }
                    .startWith(UserProfileViewEvent.Loading)
            }
            UserProfileViewEffect.LeaveHouse -> {
                leaveHouseUseCase()
                    .toFlowable()
                    .flatMap<UserProfileViewEvent> {
                        userProfileOutNavigator.goToHome()
                        never()
                    }
                    .onErrorReturn {
                        UserProfileViewEvent.LeaveHouseError
                    }
                    .observeOn(main)
                    .startWith(UserProfileViewEvent.LoadingLeaveHouse)
            }
            UserProfileViewEffect.ShowAbout -> {
                getAboutUseCase()
                    .toFlowable()
                    .map<UserProfileViewEvent> {
                        UserProfileViewEvent.ShowAbout(
                            it.toPresentation()
                        )
                    }
                    .onErrorReturn {
                        UserProfileViewEvent.LeaveHouseError
                    }
                    .observeOn(main)
            }
        }
    }
}
