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

class GetIngredientsUseCaseTest {

    private val repository: IngredientRepository = mock()
    private val useCase = GetIngredientsUseCase(repository)

    @Test
    fun `should invoke repository and return a new Ingredient when is invoked`() {
        val ingredients = listOf(TestHelper.getIngredient())
        val keyword = "keyword"
        whenever(repository.getIngredientsFirstPage(keyword)).thenReturn(Single.just(ingredients))

        useCase.invoke(keyword)
            .test()
            .assertValue(ingredients)

        verify(repository).getIngredientsFirstPage(keyword)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val keyword = "keyword"
        whenever(repository.getIngredientsFirstPage(keyword)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(keyword)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getIngredientsFirstPage(keyword)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
