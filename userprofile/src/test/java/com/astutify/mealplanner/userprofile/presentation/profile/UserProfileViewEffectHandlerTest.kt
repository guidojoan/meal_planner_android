package com.astutify.mealplanner.userprofile.presentation.profile

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import com.astutify.mealplanner.userprofile.domain.GetAboutUseCase
import com.astutify.mealplanner.userprofile.domain.GetHouseUseCase
import com.astutify.mealplanner.userprofile.domain.GetUserProfileUseCase
import com.astutify.mealplanner.userprofile.domain.LeaveHouseUseCase
import com.astutify.mealplanner.userprofile.domain.LogoutUseCase
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewEffect
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewEffectHandler
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewEvent
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class UserProfileViewEffectHandlerTest {

    private val userProfileOutNavigator: UserProfileOutNavigator = mock()
    private val getUserProfileUseCase: GetUserProfileUseCase = mock()
    private val getHouseUseCase: GetHouseUseCase = mock()
    private val logoutUseCase: LogoutUseCase = mock()
    private val leaveHouseUseCase: LeaveHouseUseCase = mock()
    private val getAboutUseCase: GetAboutUseCase = mock()
    private val effectHandler =
        UserProfileViewEffectHandler(
            Schedulers.trampoline(),
            userProfileOutNavigator,
            getUserProfileUseCase,
            getHouseUseCase,
            logoutUseCase,
            leaveHouseUseCase,
            getAboutUseCase
        )
    private val initialState =
        UserProfileViewState()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            userProfileOutNavigator,
            getUserProfileUseCase,
            getHouseUseCase,
            logoutUseCase,
            leaveHouseUseCase,
            getAboutUseCase
        )
    }

    @Test
    fun `should navigate to home when is invoked with LogOut Effect `() {
        whenever(logoutUseCase()).thenReturn(Single.just(Unit))

        effectHandler(initialState, UserProfileViewEffect.LogOut)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .assertValue(UserProfileViewEvent.LoadingLogOut)

        verify(logoutUseCase)()
        verify(userProfileOutNavigator).goToHome()
    }

    @Test
    fun `should return LogOutError Event home when is invoked with LogOut Effect and get error from backend `() {
        whenever(logoutUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler(initialState, UserProfileViewEffect.LogOut)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .assertValues(UserProfileViewEvent.LoadingLogOut, UserProfileViewEvent.LogOutError)

        verify(logoutUseCase)()
    }

    @Test
    fun `should return DataLoaded Event home when is invoked with LoadData Effect `() {
        val user = TestHelper.getUser()
        val userVM = TestHelper.getUserVM()
        val house = TestHelper.getHouse()
        val houseVM = TestHelper.getHouseVM()
        whenever(getUserProfileUseCase()).thenReturn(Single.just(user))
        whenever(getHouseUseCase()).thenReturn(Single.just(house))

        val result = effectHandler(initialState, UserProfileViewEffect.LoadData)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .values()

        verify(getUserProfileUseCase)()
        verify(getHouseUseCase)()
        assert(result[0] == UserProfileViewEvent.Loading)
        assert((result[1] as UserProfileViewEvent.DataLoaded).house == houseVM)
        assert((result[1] as UserProfileViewEvent.DataLoaded).user == userVM)
    }

    @Test
    fun `should return LoadingError Event home when is invoked with LoadData Effect and get houses return error`() {
        val user = TestHelper.getUser()
        whenever(getUserProfileUseCase()).thenReturn(Single.just(user))
        whenever(getHouseUseCase()).thenReturn(Single.error(Throwable()))

        val result = effectHandler(initialState, UserProfileViewEffect.LoadData)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .values()

        verify(getUserProfileUseCase)()
        verify(getHouseUseCase)()
        assert(result[0] == UserProfileViewEvent.Loading)
        assert(result[1] == UserProfileViewEvent.LoadingError)
    }

    @Test
    fun `should return LoadingError Event home when is invoked with LoadData Effect and get User return error`() {
        whenever(getUserProfileUseCase()).thenReturn(Single.error(Throwable()))

        val result = effectHandler(initialState, UserProfileViewEffect.LoadData)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .values()

        verify(getUserProfileUseCase)()
        assert(result[0] == UserProfileViewEvent.Loading)
        assert(result[1] == UserProfileViewEvent.LoadingError)
    }

    @Test
    fun `should navigate to home when is invoked with LeaveHouse Effect `() {
        whenever(leaveHouseUseCase()).thenReturn(Single.just(Unit))

        effectHandler(initialState, UserProfileViewEffect.LeaveHouse)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .assertValue(UserProfileViewEvent.LoadingLeaveHouse)

        verify(leaveHouseUseCase)()
        verify(userProfileOutNavigator).goToHome()
    }

    @Test
    fun `should return LeaveHouseError Event home when is invoked with LeaveHouse Effect and get error from backend `() {
        whenever(leaveHouseUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler(initialState, UserProfileViewEffect.LeaveHouse)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .assertValues(
                UserProfileViewEvent.LoadingLeaveHouse,
                UserProfileViewEvent.LeaveHouseError
            )

        verify(leaveHouseUseCase)()
    }

    @Test
    fun `should return ShowAbout Event home when is invoked with ShowAbout Effect `() {
        val about = TestHelper.getAbout()
        val aboutVM = TestHelper.getAboutVM()
        whenever(getAboutUseCase()).thenReturn(Single.just(about))

        val result = effectHandler(initialState, UserProfileViewEffect.ShowAbout)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .values()

        verify(getAboutUseCase)()
        assert((result[0] as UserProfileViewEvent.ShowAbout).about == aboutVM)
    }

    @Test
    fun `should return LeaveHouseError Event home when is invoked with ShowAbout Effect and get error from backend`() {
        whenever(getAboutUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler(initialState, UserProfileViewEffect.ShowAbout)
            .cast(UserProfileViewEvent::class.java)
            .test()
            .assertValue(UserProfileViewEvent.LeaveHouseError)

        verify(getAboutUseCase)()
    }
}
