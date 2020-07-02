package com.astutify.mealplanner.userprofile.presentation.house

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import com.astutify.mealplanner.userprofile.domain.CreateHouseUseCase
import com.astutify.mealplanner.userprofile.domain.LinkHouseUseCase
import com.astutify.mealplanner.userprofile.domain.SearchHouseUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class HouseEditEffectHandlerTest {

    private val outNavigatorComponent: UserProfileOutNavigator = mock()
    private val searchHouseUseCase: SearchHouseUseCase = mock()
    private val linkHouseUseCase: LinkHouseUseCase = mock()
    private val createHouseUseCase: CreateHouseUseCase = mock()
    private val effectHandler = HouseEditEffectHandler(
        Schedulers.trampoline(),
        outNavigatorComponent,
        searchHouseUseCase,
        linkHouseUseCase,
        createHouseUseCase
    )
    private val initialState = HouseEditViewState()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            outNavigatorComponent,
            searchHouseUseCase,
            linkHouseUseCase,
            createHouseUseCase
        )
    }

    @Test
    fun `should return a State with houses when is invoked with SearchHouse Effect and get results from API`() {
        val keyword = "keyword"
        val houses = listOf(TestHelper.getHouse())
        val housesVM = listOf(TestHelper.getHouseVM())
        whenever(searchHouseUseCase.invoke(keyword)).thenReturn(Single.just(houses))

        val result = effectHandler.invoke(initialState, HouseEditViewEffect.SearchHouse(keyword))
            .cast(HouseEditViewEvent::class.java)
            .test()
            .values()

        assert((result[0] as HouseEditViewEvent.DataLoaded).houses == housesVM)
        verify(searchHouseUseCase)(keyword)
    }

    @Test
    fun `should return a State with error when is invoked with SearchHouse Effect and get error from API`() {
        val keyword = "keyword"
        whenever(searchHouseUseCase.invoke(keyword)).thenReturn(Single.error(Throwable()))

        val result = effectHandler.invoke(initialState, HouseEditViewEffect.SearchHouse(keyword))
            .cast(HouseEditViewEvent::class.java)
            .test()
            .values()

        assert(result[0] == HouseEditViewEvent.LoadingError)
        verify(searchHouseUseCase)(keyword)
    }

    @Test
    fun `should not return anything when is invoked with SearchHouse Effect and keyword length is not valid`() {
        val keyword = "key"

        effectHandler.invoke(initialState, HouseEditViewEffect.SearchHouse(keyword))
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertNoValues()
    }

    @Test
    fun `should navigate to home when link house is successful`() {
        val houseId = "houseId"
        val joinCode = 897614
        whenever(linkHouseUseCase.invoke(houseId, joinCode)).thenReturn(Single.just(Unit))

        effectHandler.invoke(
            initialState,
            HouseEditViewEffect.JoinHouse(joinCode = joinCode, houseId = houseId)
        )
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertValue(HouseEditViewEvent.Loading)

        verify(linkHouseUseCase).invoke(houseId, joinCode)
        verify(outNavigatorComponent).goToHome()
    }

    @Test
    fun `should return JoinCodeError Event when joincode is not valid`() {
        val houseId = "houseId"
        val joinCode = 897614
        whenever(
            linkHouseUseCase.invoke(
                houseId,
                joinCode
            )
        ).thenReturn(Single.error(ApiException.NotFoundException))

        effectHandler.invoke(
            initialState,
            HouseEditViewEffect.JoinHouse(joinCode = joinCode, houseId = houseId)
        )
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertValues(HouseEditViewEvent.Loading, HouseEditViewEvent.JoinCodeError)

        verify(linkHouseUseCase).invoke(houseId, joinCode)
    }

    @Test
    fun `should return LoadingError Event when joincode is not valid`() {
        val houseId = "houseId"
        val joinCode = 897614
        whenever(linkHouseUseCase.invoke(houseId, joinCode)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(
            initialState,
            HouseEditViewEffect.JoinHouse(joinCode = joinCode, houseId = houseId)
        )
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertValues(HouseEditViewEvent.Loading, HouseEditViewEvent.LoadingError)

        verify(linkHouseUseCase).invoke(houseId, joinCode)
    }

    @Test
    fun `should navigate to home when when create house is successfull`() {
        val houseName = "houseName"
        val house = TestHelper.getHouse().copy(id = "")
        whenever(createHouseUseCase(house)).thenReturn(Single.just(Unit))

        effectHandler.invoke(initialState, HouseEditViewEffect.CreateHouse(houseName))
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertValue(HouseEditViewEvent.Loading)

        verify(createHouseUseCase)(house)
        verify(outNavigatorComponent).goToHome()
    }

    @Test
    fun `should return LoadingError Event when when create house return error from API`() {
        val houseName = "houseName"
        val house = TestHelper.getHouse().copy(id = "")
        whenever(createHouseUseCase(house)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, HouseEditViewEffect.CreateHouse(houseName))
            .cast(HouseEditViewEvent::class.java)
            .test()
            .assertValues(HouseEditViewEvent.Loading, HouseEditViewEvent.LoadingError)

        verify(createHouseUseCase)(house)
    }
}
