package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.model.data.error.ApiException
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class GetRecipesUseCaseTest {

    private val repository: RecipeRepository = mock()
    private val useCase = GetRecipesUseCase(repository)

    @Test
    fun `should invoke repository and return list of Recipes when is invoked`() {
        val recipes = listOf(TestHelper.getRecipe())
        val keywords = "keywords"
        whenever(repository.getRecipesFirstPage(keywords)).thenReturn(Single.just(recipes))

        useCase.invoke(keywords)
            .test()
            .assertValue(recipes)

        verify(repository).getRecipesFirstPage(keywords)
    }

    @Test
    fun `should invoke repository and return error when is invoked and get error from Repository`() {
        whenever(repository.getRecipesFirstPage()).thenReturn(Single.error(ApiException.InternalServerError))

        useCase.invoke()
            .test()
            .assertError {
                it is ApiException.InternalServerError
            }

        verify(repository).getRecipesFirstPage()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }
}
