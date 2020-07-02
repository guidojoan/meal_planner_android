package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.model.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class CreateHouseUseCaseTest {

    private val repository: HouseRepository = mock()
    private val sessionManager: SessionManager = mock()
    private val useCase = CreateHouseUseCase(repository, sessionManager)

    @Test
    fun `should invoke repository to create a house and set has house on session manager when invoked`() {
        val house = TestHelper.getHouse()
        whenever(repository.createHouse(house)).thenReturn(Single.just(Unit))

        useCase.invoke(house)
            .test()

        verify(repository).createHouse(house)
        verify(sessionManager).setHasHouse()
    }

    @Test
    fun `should return error when invoked and get error from repository`() {
        val house = TestHelper.getHouse()
        whenever(repository.createHouse(house)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(house)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).createHouse(house)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, sessionManager)
    }
}
