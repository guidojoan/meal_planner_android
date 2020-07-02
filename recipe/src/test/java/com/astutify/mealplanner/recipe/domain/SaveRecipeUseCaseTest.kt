package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class SaveRecipeUseCaseTest {

    private val repository: RecipeRepository = mock()
    private val useCase = SaveRecipeUseCase(repository)

    @Test
    fun `should invoke repository and return a new Recipe when is invoked`() {
        val recipe = TestHelper.getRecipe()
        whenever(repository.saveRecipe(recipe)).thenReturn(Single.just(recipe))

        useCase.invoke(recipe)
            .test()
            .assertValue(recipe)

        verify(repository).saveRecipe(recipe)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val recipe = TestHelper.getRecipe()
        whenever(repository.saveRecipe(recipe)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(recipe)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).saveRecipe(recipe)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
