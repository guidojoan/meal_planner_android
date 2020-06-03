package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class LeaveHouseUseCaseTest {

    private val repository: HouseRepository = mock()
    private val sessionManager: SessionManager = mock()
    private val useCase = LeaveHouseUseCase(repository, sessionManager)

    @Test
    fun `should invoke repository and return a Unit when is invoked`() {
        whenever(repository.unlinkHouse()).thenReturn(Single.just(Unit))

        useCase.invoke()
            .test()
            .assertNoErrors()

        verify(repository).unlinkHouse()
        verify(sessionManager).clearHasHouse()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.unlinkHouse()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).unlinkHouse()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, sessionManager)
    }
}
