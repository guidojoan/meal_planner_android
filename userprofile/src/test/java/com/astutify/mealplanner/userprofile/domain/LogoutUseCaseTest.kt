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

class LogoutUseCaseTest {

    private val repository: HouseRepository = mock()
    private val useCase = LogoutUseCase(repository)

    @Test
    fun `should invoke repository and return Unit when is invoked`() {
        whenever(repository.logout()).thenReturn(Single.just(Unit))

        useCase.invoke()
            .test()
            .assertNoErrors()

        verify(repository).logout()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.logout()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).logout()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
