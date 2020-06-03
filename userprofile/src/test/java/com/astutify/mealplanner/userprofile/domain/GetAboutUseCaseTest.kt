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

class GetAboutUseCaseTest {

    private val repository: HouseRepository = mock()
    private val useCase = GetAboutUseCase(repository)

    @Test
    fun `should invoke repository and return About when is invoked`() {
        val about = TestHelper.getAbout()
        whenever(repository.getAbout()).thenReturn(Single.just(about))

        useCase.invoke()
            .test()
            .assertValue(about)

        verify(repository).getAbout()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getAbout()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getAbout()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
