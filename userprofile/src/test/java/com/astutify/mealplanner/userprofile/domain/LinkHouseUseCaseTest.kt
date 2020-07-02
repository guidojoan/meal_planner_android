package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.model.data.error.ApiException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class LinkHouseUseCaseTest {

    private val repository: HouseRepository = mock()
    private val sessionManager: SessionManager = mock()
    private val useCase = LinkHouseUseCase(repository, sessionManager)

    @Test
    fun `should invoke repository and return a Unit when is invoked`() {
        val houseId = "houseId"
        val joinCode = 9821323
        whenever(repository.linkHouse(houseId, joinCode)).thenReturn(Single.just(Unit))

        useCase.invoke(houseId, joinCode)
            .test()
            .assertNoErrors()

        verify(repository).linkHouse(houseId, joinCode)
        verify(sessionManager).setHasHouse()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val houseId = "houseId"
        val joinCode = 9821323
        whenever(
            repository.linkHouse(
                houseId,
                joinCode
            )
        ).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(houseId, joinCode)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).linkHouse(houseId, joinCode)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, sessionManager)
    }
}
