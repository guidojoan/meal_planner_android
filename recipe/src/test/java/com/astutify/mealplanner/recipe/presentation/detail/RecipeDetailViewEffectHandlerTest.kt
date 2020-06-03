package com.astutify.mealplanner.recipe.presentation.detail

import com.astutify.mealplanner.coreui.presentation.TestHelper
import com.astutify.mealplanner.recipe.Navigator
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class RecipeDetailViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val effectHandler = RecipeDetailViewEffectHandler(navigator)
    private val initialState = RecipeDetailViewState(TestHelper.getRecipeVM())

    @Test
    fun `should navigate back when is invoked with GoBack Effect`() {
        effectHandler.invoke(initialState, RecipeDetailViewEffect.GoBack)
            .test()
            .assertNoValues()

        verify(navigator).goBack()
    }
}
