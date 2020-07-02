package com.astutify.mealplanner.ingredient.presentation.list

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import org.junit.Test

class IngredientsVewReducerTest {

    private val reducer = IngredientsVewReducer()
    private val initialState = IngredientsViewState()

    @Test
    fun `should return LoadData Effect when is invoked with LoadData Event`() {
        val result = reducer.invoke(initialState, IngredientsViewEvent.LoadData)

        assert(result.effect is IngredientsViewEffect.LoadData)
    }

    @Test
    fun `should return Search Effect when is invoked with Search Event`() {
        val keyword = "Tomato"
        val result = reducer.invoke(initialState, IngredientsViewEvent.Search(keyword))

        assert((result.effect as IngredientsViewEffect.Search).name == keyword)
    }

    @Test
    fun `should return Loading State when is invoked with Loading Event`() {
        val expectedState = initialState.copyState(
            loading = IngredientsViewState.Loading.LOADING
        )
        val result = reducer.invoke(initialState, IngredientsViewEvent.Loading)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with ingredients when is invoked with DataLoaded Event`() {
        val event = IngredientsViewEvent.DataLoaded(listOf(TestHelper.getIngredientVM()))
        val expectedState = initialState.copyState(
            error = null,
            ingredients = event.ingredients
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return empty State when is invoked with DataLoaded Event and ingredients is empty`() {
        val event = IngredientsViewEvent.DataLoaded(listOf())
        val expectedState = initialState.copyState(
            error = IngredientsViewState.Error.NO_RESULTS,
            ingredients = event.ingredients
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return LoadingError State when is invoked with LoadingError Event`() {
        val expectedState = initialState.copyState(
            error = IngredientsViewState.Error.LOADING_ERROR
        )
        val result = reducer.invoke(initialState, IngredientsViewEvent.LoadingError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return GoToAddIngredient Effect when is invoked with ClickAddIngredient Event`() {
        val result = reducer.invoke(initialState, IngredientsViewEvent.ClickAddIngredient)

        assert(result.effect is IngredientsViewEffect.GoToAddIngredient)
    }

    @Test
    fun `should return GoToEditIngredient Effect when is invoked with IngredientClicked Event`() {
        val ingredient = TestHelper.getIngredientVM()
        val result =
            reducer.invoke(initialState, IngredientsViewEvent.IngredientClicked(ingredient))

        assert((result.effect as IngredientsViewEffect.GoToEditIngredient).ingredient == ingredient)
    }

    @Test
    fun `should return State with Ingredient when is invoked with IngredientAdded Event`() {
        val ingredient = TestHelper.getIngredientVM()
        val result = reducer.invoke(initialState, IngredientsViewEvent.IngredientAdded(ingredient))

        assert(result.state!!.ingredients.first() == ingredient)
    }

    @Test
    fun `should return State with ingredient updated when is invoked with IngredientUpdated Event`() {
        val ingredient = TestHelper.getIngredientVM()
        val ingredientUpdated = ingredient.copy(name = "nameUpdated")
        val initialState = initialState.copyState(ingredients = listOf(ingredient))
        val result =
            reducer.invoke(initialState, IngredientsViewEvent.IngredientUpdated(ingredientUpdated))

        assert(result.state!!.ingredients.first() == ingredientUpdated)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickRefresh Event`() {
        val result = reducer.invoke(initialState, IngredientsViewEvent.ClickRefresh)

        assert(result.effect is IngredientsViewEffect.LoadData)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickCloseSearch Event`() {
        val result = reducer.invoke(initialState, IngredientsViewEvent.ClickCloseSearch)

        assert(result.effect is IngredientsViewEffect.LoadData)
    }
}
