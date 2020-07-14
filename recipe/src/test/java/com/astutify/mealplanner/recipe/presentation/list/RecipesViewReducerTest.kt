package com.astutify.mealplanner.recipe.presentation.list

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewEffect
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewEvent
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewReducer
import com.astutify.mealplanner.recipe.presentation.list.mvi.RecipesViewState
import org.junit.Test

class RecipesViewReducerTest {

    private val reducer =
        RecipesViewReducer()
    private val initialState =
        RecipesViewState()

    @Test
    fun `should return CheckLoginStatus Effect when is invoked with CheckLoginStatus Event`() {
        val result = reducer.invoke(initialState, RecipesViewEvent.CheckLoginStatus)

        assert(result.effect is RecipesViewEffect.CheckLoginStatus)
    }

    @Test
    fun `should return LoadData Effect when is invoked with Load Event`() {
        val result = reducer.invoke(initialState, RecipesViewEvent.Load)

        assert(result.effect is RecipesViewEffect.LoadData)
    }

    @Test
    fun `should return LoadError State when is invoked with LoadingError Event`() {
        val expectedState = initialState.copyState(error = RecipesViewState.Error.LOADING_ERROR)

        val result = reducer.invoke(initialState, RecipesViewEvent.LoadingError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with recipes when is invoked with DataLoaded Event`() {
        val event = RecipesViewEvent.DataLoaded(listOf(TestHelper.getRecipeVM()))
        val expectedState = initialState.copyState(
            recipes = event.recipes,
            error = null
        )

        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State empty when is invoked with DataLoaded Event and recipes is empty`() {
        val event = RecipesViewEvent.DataLoaded(listOf())
        val expectedState = initialState.copyState(
            recipes = event.recipes,
            error = RecipesViewState.Error.NO_RESULTS
        )

        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return GoToAddRecipe Effect when is invoked with ClickAddRecipe Event`() {
        val result = reducer.invoke(initialState, RecipesViewEvent.ClickAddRecipe)

        assert(result.effect is RecipesViewEffect.GoToAddRecipe)
    }

    @Test
    fun `should return NetworkError State when is invoked with NetworkError Event`() {
        val expectedState = initialState.copyState(error = RecipesViewState.Error.LOADING_ERROR)

        val result = reducer.invoke(initialState, RecipesViewEvent.NetworkError)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return Loading State when is invoked with Loading Event`() {
        val expectedState = initialState.copyState(loading = RecipesViewState.Loading.LOADING)

        val result = reducer.invoke(initialState, RecipesViewEvent.Loading)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return GoToRecipeDetail Effect when is invoked with RecipeClicked Event`() {
        val result =
            reducer.invoke(initialState, RecipesViewEvent.RecipeClicked(TestHelper.getRecipeVM()))

        assert(result.effect is RecipesViewEffect.GoToRecipeDetail)
    }

    @Test
    fun `should return State with +1 Recipe when is invoked with RecipeAdded Event`() {
        val newRecipe = TestHelper.getRecipeVM()
        val expectedState = initialState.copyState(recipes = listOf(newRecipe))

        val result = reducer.invoke(initialState, RecipesViewEvent.RecipeAdded(newRecipe))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return GoToEditRecipe Effect when is invoked with RecipeLongClicked Event`() {
        val result = reducer.invoke(
            initialState,
            RecipesViewEvent.RecipeLongClicked(
                TestHelper.getRecipeVM()
            )
        )

        assert(result.effect is RecipesViewEffect.GoToEditRecipe)
    }

    @Test
    fun `should return State with updated recipe when is invoked with RecipeUpdated Event`() {
        val recipe = TestHelper.getRecipeVM()
        val recipeUpdated = TestHelper.getRecipeVM().copy(name = "nameUpdated")
        val initialState = initialState.copyState(recipes = listOf(recipe))
        val expectedState = initialState.copyState(recipes = listOf(recipeUpdated))

        val result = reducer.invoke(initialState, RecipesViewEvent.RecipeUpdated(recipeUpdated))

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with -1 Recipe when is invoked with RecipeDeleted Event`() {
        val recipeToDelete = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipes = listOf(recipeToDelete))
        val event = RecipesViewEvent.RecipeDeleted(recipeToDelete)
        val expectedState =
            newInitialState.copyState(recipes = listOf(), error = RecipesViewState.Error.NO_RESULTS)

        val result = reducer.invoke(newInitialState, event)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickRefresh Event`() {
        val result = reducer.invoke(initialState, RecipesViewEvent.ClickRefresh)

        assert(result.effect is RecipesViewEffect.LoadData)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickCloseSearch Event`() {
        val result = reducer.invoke(initialState, RecipesViewEvent.ClickCloseSearch)

        assert(result.effect is RecipesViewEffect.LoadData)
    }

    @Test
    fun `should return LoadData Effect when is invoked with Search Event`() {
        val searchKey = "Pizza"

        val result = reducer.invoke(initialState, RecipesViewEvent.Search(searchKey))

        assert((result.effect as RecipesViewEffect.Search).name == searchKey)
    }
}
