package com.astutify.mealplanner.recipe.presentation.detail

import com.astutify.mealplanner.coreui.presentation.TestHelper
import org.junit.Test

class RecipeDetailViewReducerTest {

    private val recipe = TestHelper.getRecipeVM()
    private val reducer = RecipeDetailViewReducer(recipe)
    private val initialState = RecipeDetailViewState(recipe)

    @Test
    fun `should return GoBack Effect when is invoked with ClickBack Event`() {
        val event = RecipeDetailViewEvent.ClickBack

        val state = reducer(initialState, event)

        assert(state.effect is RecipeDetailViewEffect.GoBack)
    }

    @Test
    fun `should return State with +- servings when is invoked with ServingsChanged Event`() {
        val event = RecipeDetailViewEvent.ServingsChanged(8)
        val expectedState = initialState.copy(recipe = recipe.copyWithNewServings(event.servings))

        val state = reducer(initialState, event)

        assert(state.state == expectedState)
    }
}
