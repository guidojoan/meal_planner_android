package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class GetHouseUseCaseTest {

    private val repository: HouseRepository = mock()
    private val useCase = GetHouseUseCase(repository)

    @Test
    fun `should invoke repository and return House when is invoked`() {
        val house = TestHelper.getHouse()
        whenever(repository.getHouse()).thenReturn(Single.just(house))

        useCase.invoke()
            .test()
            .assertValue(house)

        verify(repository).getHouse()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getHouse()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getHouse()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
