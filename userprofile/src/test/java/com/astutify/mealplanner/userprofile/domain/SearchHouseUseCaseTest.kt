package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.model.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class SearchHouseUseCaseTest {

    private val repository: HouseRepository = mock()
    private val useCase = SearchHouseUseCase(repository)

    @Test
    fun `should invoke repository and return a list of houses when is invoked`() {
        val houses = listOf(TestHelper.getHouse())
        val keywords = "keywords"
        whenever(repository.searchHouse(keywords)).thenReturn(Single.just(houses))

        useCase.invoke(keywords)
            .test()
            .assertValue(houses)

        verify(repository).searchHouse(keywords)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val keywords = "keywords"
        whenever(repository.searchHouse(keywords)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(keywords)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).searchHouse(keywords)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
