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

class UpdateIngredientUseCaseTest {

    private val repository: IngredientRepository = mock()
    private val useCase = UpdateIngredientUseCase(repository)

    @Test
    fun `should invoke repository and return a new Ingredient when is invoked`() {
        val ingredient = TestHelper.getIngredient()
        whenever(repository.updateIngredient(ingredient)).thenReturn(Single.just(ingredient))

        useCase.invoke(ingredient)
            .test()
            .assertValue(ingredient)

        verify(repository).updateIngredient(ingredient)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val ingredient = TestHelper.getIngredient()
        whenever(repository.updateIngredient(ingredient)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(ingredient)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).updateIngredient(ingredient)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
