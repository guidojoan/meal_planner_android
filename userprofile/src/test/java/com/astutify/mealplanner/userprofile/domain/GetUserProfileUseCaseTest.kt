package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class GetUserProfileUseCaseTest {

    private val repository: HouseRepository = mock()
    private val useCase = GetUserProfileUseCase(repository)

    @Test
    fun `should invoke repository and return User when is invoked`() {
        val user = TestHelper.getUser()
        whenever(repository.getUserProfile()).thenReturn(Single.just(user))

        useCase.invoke()
            .test()
            .assertValue(user)

        verify(repository).getUserProfile()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getUserProfile()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getUserProfile()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
