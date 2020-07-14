package com.astutify.mealplanner.userprofile.presentation.house

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewEffect
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewEvent
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewReducer
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewState
import org.junit.Test

class HouseEditViewReducerTest {

    private val reducer =
        HouseEditViewReducer()
    private val initialState =
        HouseEditViewState()

    @Test
    fun `should return State with house name and SearchHouse Effect when invoked with OnNameChanged Event`() {
        val houseName = "Nueva Casa"
        val expectedState = initialState.copyState(
            houseName = houseName,
            joinCode = 0,
            joinCodeVisible = false
        )
        val expectedEffect = HouseEditViewEffect.SearchHouse(houseName)

        val result = reducer(initialState, HouseEditViewEvent.OnNameChanged(houseName))

        assert(result.state == expectedState)
        assert(result.effect == expectedEffect)
    }

    @Test
    fun `should return State with selected house when invoked with OnNameChanged Event and the house exists on the list`() {
        val houseSelected = TestHelper.getHouseVM()
        val initialState = initialState.copyState(houses = listOf(houseSelected))
        val houseName = "houseName"
        val expectedState = initialState.copyState(
            houseSelected = houseSelected,
            continueEnabled = true,
            joinCodeVisible = false
        )

        val result = reducer(initialState, HouseEditViewEvent.OnNameChanged(houseName))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return JoinHouse Effect when is invoked with ContinueClicked and house is selected and joincode length is valid`() {
        val houseSelected = TestHelper.getHouseVM()
        val joinCode = 9878123
        val initialState =
            initialState.copyState(houseSelected = houseSelected, joinCode = joinCode)
        val expectedEffect = HouseEditViewEffect.JoinHouse(
            houseSelected.id,
            joinCode
        )

        val result = reducer(initialState, HouseEditViewEvent.ContinueClicked)
        assert(result.effect == expectedEffect)
    }

    @Test
    fun `should return State with joinCode visible when is invoked with ContinueClicked and house is selected`() {
        val houseSelected = TestHelper.getHouseVM()
        val initialState = initialState.copyState(houseSelected = houseSelected)
        val expectedState = initialState.copyState(joinCodeVisible = true, continueEnabled = false)

        val result = reducer(initialState, HouseEditViewEvent.ContinueClicked)
        assert(result.state == expectedState)
    }

    @Test
    fun `should return CreateHouse Effect when is invoked with ContinueClicked and house is not selected`() {
        val houseName = "Casona nova"
        val initialState = initialState.copyState(houseName = houseName)
        val expectedEffect = HouseEditViewEffect.CreateHouse(houseName)

        val result = reducer(initialState, HouseEditViewEvent.ContinueClicked)
        assert(result.effect == expectedEffect)
    }

    @Test
    fun `should return State with a list of houses and continue enabled when is invoked with DataLoaded Event and house name length is valid`() {
        val houseName = "Casona nova 2020"
        val houses = listOf(TestHelper.getHouseVM())
        val initialState = initialState.copyState(houseName = houseName)
        val expectedState = initialState.copyState(
            houses = houses,
            continueEnabled = true,
            houseSelected = null
        )

        val result = reducer(initialState, HouseEditViewEvent.DataLoaded(houses))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with a list of houses and continue disabled when is invoked with DataLoaded Event and house name length is not valid`() {
        val houseName = "Casa"
        val houses = listOf(TestHelper.getHouseVM())
        val initialState = initialState.copyState(houseName = houseName)
        val expectedState = initialState.copyState(
            houses = houses,
            continueEnabled = false,
            houseSelected = null
        )

        val result = reducer(initialState, HouseEditViewEvent.DataLoaded(houses))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with a list of houses and continue disabled when is invoked with DataLoaded Event and house exists on list`() {
        val houseName = "houseName"
        val houses = listOf(TestHelper.getHouseVM())
        val initialState = initialState.copyState(houseName = houseName)
        val expectedState = initialState.copyState(
            houses = houses,
            continueEnabled = false,
            houseSelected = null
        )

        val result = reducer(initialState, HouseEditViewEvent.DataLoaded(houses))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State loading error when invoked with LoadingError Event `() {
        val expectedState = initialState.copyState(
            error = HouseEditViewState.Error.NETWORK
        )

        val result = reducer(initialState, HouseEditViewEvent.LoadingError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State loading when invoked with Loading Event `() {
        val expectedState = initialState.copyState(
            loading = HouseEditViewState.Loading.LOADING
        )

        val result = reducer(initialState, HouseEditViewEvent.Loading)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with joincode and button enabled when invoked with OnJoinCodeChanged Event and joincode length is valid`() {
        val joincode = 897652
        val expectedState = initialState.copyState(
            joinCode = joincode,
            continueEnabled = true
        )

        val result = reducer(initialState, HouseEditViewEvent.OnJoinCodeChanged(joincode))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with joincode and button disabled when invoked with OnJoinCodeChanged Event and joincode length is not valid`() {
        val joincode = 897
        val expectedState = initialState.copyState(
            joinCode = joincode,
            continueEnabled = false
        )

        val result = reducer(initialState, HouseEditViewEvent.OnJoinCodeChanged(joincode))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with error when invoked with JoinCodeError Event `() {
        val expectedState = initialState.copyState(
            error = HouseEditViewState.Error.JOIN_CODE
        )

        val result = reducer(initialState, HouseEditViewEvent.JoinCodeError)

        assert(result.state == expectedState)
    }
}
