package com.astutify.mealplanner.recipe.presentation.edit

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewEffect
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewEvent
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewReducer
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewState
import org.junit.Test

class EditRecipeViewReducerTest {

    private val reducer =
        EditRecipeViewReducer()
    private val initialState =
        EditRecipeViewState()

    @Test
    fun `should return State with directions when receive DirectionsChanged Event`() {
        val event = EditRecipeViewEvent.DirectionsChanged("directions")
        val expectedState = initialState.copyState(
            recipe = initialState.recipe.copyWithDirections(event.directions),
            saveEnabled = false
        )
        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with name when receive NameChanged Event`() {
        val event = EditRecipeViewEvent.NameChanged("newName")
        val expectedState = initialState.copyState(
            recipe = initialState.recipe.copyWithName(event.name),
            saveEnabled = false
        )
        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with +- servings when receive ServingsChanged Event`() {
        val event = EditRecipeViewEvent.ServingsChanged(5)
        val expectedState = initialState.copyState(
            recipe = initialState.recipe.copyWithServings(event.servings),
            saveEnabled = false
        )
        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with + ingredient when receive IngredientAdded Event`() {
        val recipe = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipe)
        val newIngredient = TestHelper.getRecipeIngredientVM()
        val event =
            EditRecipeViewEvent.IngredientAdded(newIngredient, recipe.ingredientGroups.first().id)
        val expectedState = newInitialState.copyState(
            recipe = initialState.recipe.copyWithIngredient(
                event.ingredientGroupId,
                event.recipeIngredient
            ),
            saveEnabled = false
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with error when receive ClickSave Event and image is empty`() {
        val event = EditRecipeViewEvent.ClickSave
        val expectedState = initialState.copyState(
            error = EditRecipeViewState.Error.IMAGE_NOT_SELECTED
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return SaveRecipe Effect when receive ClickSave Event`() {
        val recipe = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipe)
        val event = EditRecipeViewEvent.ClickSave

        val state = reducer.invoke(newInitialState, event)

        assert((state.effect as EditRecipeViewEffect.SaveRecipe).recipe == recipe)
    }

    @Test
    fun `should return GoToRecipeIngredients Effect when receive NewIngredientClicked Event`() {
        val ingredientGroupId = "ingredientGroupId"
        val event = EditRecipeViewEvent.NewIngredientClicked(ingredientGroupId)

        val state = reducer.invoke(initialState, event)

        assert((state.effect as EditRecipeViewEffect.GoToRecipeIngredients).ingredientGroupId == ingredientGroupId)
    }

    @Test
    fun `should return State with error when receive OnSaveError Event`() {
        val event = EditRecipeViewEvent.OnSaveError
        val expectedState = initialState.copyState(
            error = EditRecipeViewState.Error.SAVE,
            saveEnabled = true
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return loading State when receive Loading Event`() {
        val event = EditRecipeViewEvent.Loading
        val expectedState = initialState.copyState(
            loading = EditRecipeViewState.Loading.LOADING,
            saveEnabled = false
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with + IngredientGroup when receive IngredientGroupAdded Event`() {
        val groupName = "groupName"
        val event = EditRecipeViewEvent.IngredientGroupAdded(groupName)

        val state = reducer.invoke(initialState, event)

        assert(state.state!!.recipe.ingredientGroups.size == 1)
    }

    @Test
    fun `should return State with - Ingredient when receive RecipeIngredientRemoveClick Event`() {
        val recipe = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipe)
        val event =
            EditRecipeViewEvent.RecipeIngredientRemoveClick(
                recipe.ingredientGroups.first()
                    .recipeIngredients.first().id
            )
        val expectedState = newInitialState.copyState(
            recipe = newInitialState.recipe.copyWithOutIngredient(event.recipeIngredientId),
            saveEnabled = false
        )

        val state = reducer.invoke(newInitialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with - IngredientGroup when receive IngredientGroupRemoveClick Event`() {
        val recipe = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipe)
        val event =
            EditRecipeViewEvent.IngredientGroupRemoveClick(recipe.ingredientGroups.first().id)
        val expectedState = newInitialState.copyState(
            recipe = newInitialState.recipe.copyWithOutIngredientGroup(event.ingredientGroupId),
            saveEnabled = false
        )

        val state = reducer.invoke(newInitialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with imageUrl when receive ImagePicked Event`() {
        val event = EditRecipeViewEvent.ImagePicked("url")
        val expectedState = initialState.copyState(
            recipe = initialState.recipe.copy(imageUrl = event.imageUrl)
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return LoadData Effect when is invoked with LoadData Event`() {
        val event = EditRecipeViewEvent.LoadData

        val state = reducer.invoke(initialState, event)

        assert(state.effect is EditRecipeViewEffect.LoadData)
    }

    @Test
    fun `should return State with error when receive LoadingError Event`() {
        val event = EditRecipeViewEvent.LoadingError
        val expectedState = initialState.copyState(
            error = EditRecipeViewState.Error.LOADING
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with RecipesCategories when receive DataLoaded Event`() {
        val event = EditRecipeViewEvent.DataLoaded(listOf(TestHelper.getRecipeCategoryVM()))
        val expectedState = initialState.copyState(
            recipeCategories = event.recipeCategories
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with Loading when receive LoadingSave Event`() {
        val event = EditRecipeViewEvent.LoadingSave
        val expectedState = initialState.copyState(
            loading = EditRecipeViewState.Loading.SAVE
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return State with Category when receive CategorySelected Event`() {
        val event = EditRecipeViewEvent.CategorySelected(TestHelper.getRecipeCategoryVM())
        val expectedState = initialState.copyState(
            recipe = initialState.recipe.copy(recipeCategory = event.recipeCategory)
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return GoBack Effect when receive ClickBack Event`() {
        val event = EditRecipeViewEvent.ClickBack

        val state = reducer.invoke(initialState, event)

        assert(state.effect is EditRecipeViewEffect.GoBack)
    }

    @Test
    fun `should return State with Alert when receive ClickDelete Event`() {
        val event = EditRecipeViewEvent.ClickDelete
        val expectedState = initialState.copyState(
            message = EditRecipeViewState.Message.DELETE_ALERT
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }

    @Test
    fun `should return Delete Effect when receive ConfirmDelete Event`() {
        val event = EditRecipeViewEvent.ConfirmDelete

        val state = reducer.invoke(initialState, event)

        assert(state.effect is EditRecipeViewEffect.Delete)
    }

    @Test
    fun `should return State with Error when receive ErrorNameTaken Event`() {
        val event = EditRecipeViewEvent.ErrorNameTaken
        val expectedState = initialState.copyState(
            error = EditRecipeViewState.Error.NAME_TAKEN
        )

        val state = reducer.invoke(initialState, event)

        assert(state.state == expectedState)
    }
}
