package com.astutify.mealplanner.userprofile.presentation.profile

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import org.junit.Test

class UserProfileViewReducerTest {

    private val reducer = UserProfileViewReducer()
    private val initialState = UserProfileViewState()

    @Test
    fun `should get Logout Effect when invoked with LogOut Event`() {
        val result = reducer(initialState, UserProfileViewEvent.LogOut)

        assert(result.effect is UserProfileViewEffect.LogOut)
    }

    @Test
    fun `should get LeaveHouse Effect when invoked with LeaveHouse Event`() {
        val result = reducer(initialState, UserProfileViewEvent.LeaveHouse)

        assert(result.effect is UserProfileViewEffect.LeaveHouse)
    }

    @Test
    fun `should get LoadData Effect when invoked with LoadData Event`() {
        val result = reducer(initialState, UserProfileViewEvent.LoadData)

        assert(result.effect is UserProfileViewEffect.LoadData)
    }

    @Test
    fun `should get Loading State when invoked with Loading Event`() {
        val expectedState = initialState.copyState(loading = UserProfileViewState.Loading.LOADING)

        val result = reducer(initialState, UserProfileViewEvent.Loading)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get LoadingLogOut State when invoked with LoadingLogOut Event`() {
        val expectedState =
            initialState.copyState(loading = UserProfileViewState.Loading.LOADING_LOGOUT)

        val result = reducer(initialState, UserProfileViewEvent.LoadingLogOut)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get LoadingLeaveHouse State when invoked with LoadingLeaveHouse Event`() {
        val expectedState =
            initialState.copyState(loading = UserProfileViewState.Loading.LOADING_LEAVE_HOUSE)

        val result = reducer(initialState, UserProfileViewEvent.LoadingLeaveHouse)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get DataLoaded State when invoked with DataLoaded Event`() {
        val user = TestHelper.getUserVM()
        val house = TestHelper.getHouseVM()
        val expectedState = initialState.copyState(user = user, houses = house)

        val result = reducer(initialState, UserProfileViewEvent.DataLoaded(user, house))

        assert(result.state == expectedState)
    }

    @Test
    fun `should get LoadingError State when invoked with LoadingError Event`() {
        val expectedState = initialState.copyState(error = UserProfileViewState.Error.LOADING_ERROR)

        val result = reducer(initialState, UserProfileViewEvent.LoadingError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get LogOutError State when invoked with LogOutError Event`() {
        val expectedState = initialState.copyState(error = UserProfileViewState.Error.LOGOUT_ERROR)

        val result = reducer(initialState, UserProfileViewEvent.LogOutError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get LeaveHouseError State when invoked with LeaveHouseError Event`() {
        val expectedState =
            initialState.copyState(error = UserProfileViewState.Error.LEAVE_HOUSE_ERROR)

        val result = reducer(initialState, UserProfileViewEvent.LeaveHouseError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should get ShowAbout Effect when invoked with ClickCard Event`() {
        val result = reducer(initialState, UserProfileViewEvent.ClickCard)

        assert(result.effect is UserProfileViewEffect.ShowAbout)
    }

    @Test
    fun `should get ShowAbout State when invoked with ShowAbout Event`() {
        val about = TestHelper.getAboutVM()
        val expectedState = initialState.copyState(about = about)

        val result = reducer(initialState, UserProfileViewEvent.ShowAbout(about))

        assert(result.state == expectedState)
    }
}
