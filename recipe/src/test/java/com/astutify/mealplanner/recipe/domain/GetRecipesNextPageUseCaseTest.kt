package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class GetRecipesNextPageUseCaseTest {

    private val repository: RecipeRepository = mock()
    private val useCase = GetRecipesNextPageUseCase(repository)

    @Test
    fun `should invoke repository and return list of Recipes when is invoked`() {
        val recipes = listOf(TestHelper.getRecipe())
        whenever(repository.getRecipesNextPage()).thenReturn(Single.just(recipes))

        useCase.invoke()
            .test()
            .assertValue(recipes)

        verify(repository).getRecipesNextPage()
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getRecipesNextPage()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getRecipesNextPage()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
