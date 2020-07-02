package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.model.data.error.ApiException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class DeleteRecipesUseCaseTest {

    private val repository: RecipeRepository = mock()
    private val useCase = DeleteRecipesUseCase(repository)

    @Test
    fun `should invoke repository and return Unit when is invoked`() {
        val recipeId = "recipeId"
        whenever(repository.deleteRecipe(recipeId)).thenReturn(Single.just(Unit))

        useCase.invoke(recipeId)
            .test()
            .assertNoErrors()

        verify(repository).deleteRecipe(recipeId)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        val recipeId = "recipeId"
        whenever(repository.deleteRecipe(recipeId)).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke(recipeId)
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).deleteRecipe(recipeId)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
