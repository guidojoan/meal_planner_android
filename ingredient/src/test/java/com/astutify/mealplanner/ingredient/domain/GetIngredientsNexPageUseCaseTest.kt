package com.astutify.mealplanner.ingredient.domain

import com.astutify.mealplanner.core.model.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class GetIngredientsNexPageUseCaseTest {

    private val repository: IngredientRepository = mock()
    private val useCase = GetIngredientsNexPageUseCase(repository)

    @Test
    fun `should invoke repository and return a new Ingredient when is invoked`() {
        val ingredients = listOf(TestHelper.getIngredient())
        whenever(repository.getIngredientsNextPage()).thenReturn(Single.just(ingredients))

        useCase.invoke()
            .test()
            .assertValue(ingredients)

        verify(repository).getIngredientsNextPage()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getIngredientsNextPage()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getIngredientsNextPage()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
