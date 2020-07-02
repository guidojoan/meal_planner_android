package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.coreui.presentation.mvi.Next
import com.astutify.mealplanner.coreui.presentation.mvi.State
import org.junit.Test

class RecipeIngredientsViewReducerTest {

    private val reducer = RecipeIngredientsViewReducer()
    private val initialState = RecipeIngredientsViewState()

    @Test
    fun `should return a list of ingredients when is invoked with DataLoaded event`() {
        val event = RecipeIngredientsViewEvent.DataLoaded(getIngredients())
        val expectedState = State<RecipeIngredientsViewState, RecipeIngredientsViewEffect>(
            state = initialState.copy(ingredients = event.ingredients)
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState.state)
    }

    @Test
    fun `should return Search Effect when is invoked with Search event`() {
        val event = RecipeIngredientsViewEvent.Search(SEARCH_STRING)
        val expectedState = Next(
            state = initialState.copy(selectedIngredient = null),
            effect = RecipeIngredientsViewEffect.Search(event.name)
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState.state)
        assert(state.effect is RecipeIngredientsViewEffect.Search)
    }

    @Test
    fun `should return a State with selected ingredient when is invoked with IngredientClick event`() {
        val event = RecipeIngredientsViewEvent.IngredientClick(IngredientViewModel())
        val expectedState = State<RecipeIngredientsViewState, RecipeIngredientsViewEffect>(
            state = initialState.copy(selectedIngredient = event.ingredient)
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState.state)
    }

    @Test
    fun `should return a Effect with ingredient when is invoked with IngredientQuantitySet event`() {
        val ingredient = TestHelper.getIngredientVM()
        val measurement = TestHelper.getPrimaryMeasurementVM()
        val initialState = initialState.copy(selectedIngredient = ingredient)
        val event = RecipeIngredientsViewEvent.IngredientQuantitySet(QUANTITY, measurement)

        val state = reducer.invoke(initialState, event)
        val effect = state.effect as RecipeIngredientsViewEffect.IngredientQuantitySet

        assert(effect.ingredient == ingredient)
        assert(effect.quantity == QUANTITY)
        assert(effect.measurement == measurement)
    }

    @Test
    fun `should return ClickBack Effect with is invoked with ClickBack Event`() {
        val event = RecipeIngredientsViewEvent.ClickBack

        val state = reducer.invoke(initialState, event)

        assert(state.effect is RecipeIngredientsViewEffect.ClickBack)
    }

    @Test
    fun `should return GoToAddIngredient Effect when is invoked with ClickAddIngredient Event`() {
        val event = RecipeIngredientsViewEvent.ClickAddIngredient

        val state = reducer.invoke(initialState, event)

        assert(state.effect is RecipeIngredientsViewEffect.GoToAddIngredient)
    }

    private fun getIngredients() = listOf(IngredientViewModel())

    companion object {
        const val SEARCH_STRING = "searchString"
        const val QUANTITY = 1f
    }
}
