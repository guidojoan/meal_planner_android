package com.astutify.mealplanner.recipe.presentation.edit

import com.astutify.mealplanner.core.domain.GetRecipeCategoriesUseCase
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.recipe.presentation.Navigator
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.recipe.domain.DeleteRecipesUseCase
import com.astutify.mealplanner.recipe.domain.SaveRecipeUseCase
import com.astutify.mealplanner.recipe.domain.UpdateRecipeUseCase
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewEffect
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewEffectHandler
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewEvent
import com.astutify.mealplanner.recipe.presentation.edit.mvi.EditRecipeViewState
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class EditRecipeViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val outNavigator: RecipeOutNavigator = mock()
    private val saveRecipeUseCase: SaveRecipeUseCase = mock()
    private val getRecipeCategoriesUseCase: GetRecipeCategoriesUseCase = mock()
    private val updateRecipeUseCase: UpdateRecipeUseCase = mock()
    private val deleteRecipesUseCase: DeleteRecipesUseCase = mock()

    private val effectHandler =
        EditRecipeViewEffectHandler(
            Schedulers.trampoline(),
            navigator,
            outNavigator,
            saveRecipeUseCase,
            getRecipeCategoriesUseCase,
            updateRecipeUseCase,
            deleteRecipesUseCase
        )
    private val initialState =
        EditRecipeViewState()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            navigator,
            outNavigator,
            saveRecipeUseCase,
            getRecipeCategoriesUseCase,
            updateRecipeUseCase,
            deleteRecipesUseCase
        )
    }

    @Test
    fun `should return a Recipe when save is successful`() {
        val recipeVM = TestHelper.getRecipeVM()
        val recipe = TestHelper.getRecipe()
        val captor = argumentCaptor<ActivityResult<RecipeViewModel>>()
        val expected = ActivityResult(
            recipe.toPresentation().copy(status = RecipeViewModel.Status.NEW)
        )
        whenever(saveRecipeUseCase.invoke(recipe)).thenReturn(Single.just(recipe))

        effectHandler.invoke(initialState, EditRecipeViewEffect.SaveRecipe(recipeVM))
            .cast(EditRecipeViewEvent::class.java)
            .test()

        verify(saveRecipeUseCase).invoke(recipe)
        verify(navigator).finishEditRecipe(captor.capture())
        assert(captor.firstValue == expected)
    }

    @Test
    fun `should return a Recipe when update is successful`() {
        val recipeVM = TestHelper.getRecipeVM()
        val recipe = TestHelper.getRecipe()
        val initialState = initialState.copyState(mode = EditRecipeViewState.Mode.EDIT)
        val captor = argumentCaptor<ActivityResult<RecipeViewModel>>()
        val expected = ActivityResult(
            recipe.toPresentation().copy(status = RecipeViewModel.Status.UPDATED)
        )
        whenever(updateRecipeUseCase.invoke(recipe)).thenReturn(Single.just(recipe))

        effectHandler.invoke(initialState, EditRecipeViewEffect.SaveRecipe(recipeVM))
            .cast(EditRecipeViewEvent::class.java)
            .test()

        verify(updateRecipeUseCase).invoke(recipe)
        verify(navigator).finishEditRecipe(captor.capture())
        assert(captor.firstValue == expected)
    }

    @Test
    fun `should return OnSaveError when receive error from Backend`() {
        val recipeVM = TestHelper.getRecipeVM()
        val recipe = TestHelper.getRecipe()
        whenever(saveRecipeUseCase.invoke(recipe)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, EditRecipeViewEffect.SaveRecipe(recipeVM))
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertValues(EditRecipeViewEvent.LoadingSave, EditRecipeViewEvent.OnSaveError)

        verify(saveRecipeUseCase).invoke(recipe)
    }

    @Test
    fun `should return Error Name Taken when receive BadRequest from backend`() {
        val recipeVM = TestHelper.getRecipeVM()
        val recipe = TestHelper.getRecipe()
        whenever(saveRecipeUseCase.invoke(recipe)).thenReturn(Single.error(TestHelper.getNameTakenError()))

        effectHandler.invoke(initialState, EditRecipeViewEffect.SaveRecipe(recipeVM))
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertValues(EditRecipeViewEvent.LoadingSave, EditRecipeViewEvent.ErrorNameTaken)

        verify(saveRecipeUseCase).invoke(recipe)
    }

    @Test
    fun `should navigate to AddRecipeIngredient when receive GoToRecipeIngredients Effect`() {
        val ingredientGroupId = "ingredientGroupId"

        effectHandler.invoke(
            initialState,
            EditRecipeViewEffect.GoToRecipeIngredients(ingredientGroupId)
        )
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(outNavigator).goToAddRecipeIngredient(ingredientGroupId)
    }

    @Test
    fun `should return State with categories when is invoked with LoadData Effect`() {
        val recipeCategories = listOf(TestHelper.getRecipeCategory())
        val recipeCategoriesVM = listOf(TestHelper.getRecipeCategoryVM())
        whenever(getRecipeCategoriesUseCase.invoke()).thenReturn(Single.just(recipeCategories))

        val result = effectHandler.invoke(initialState, EditRecipeViewEffect.LoadData)
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .values()

        assert(result[0] is EditRecipeViewEvent.Loading)
        assert((result[1] as EditRecipeViewEvent.DataLoaded).recipeCategories == recipeCategoriesVM)
        verify(getRecipeCategoriesUseCase).invoke()
    }

    @Test
    fun `should return Error when is invoked with LoadData Effect and get error from backend`() {
        whenever(getRecipeCategoriesUseCase.invoke()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, EditRecipeViewEffect.LoadData)
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertValues(EditRecipeViewEvent.Loading, EditRecipeViewEvent.LoadingError)

        verify(getRecipeCategoriesUseCase).invoke()
    }

    @Test
    fun `should navigate back when is invoked with GoBack Event`() {
        effectHandler.invoke(initialState, EditRecipeViewEffect.GoBack)
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goBack()
    }

    @Test
    fun `should return Error when invoke Delete and get error from Backend`() {
        val recipe = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipe)
        whenever(deleteRecipesUseCase.invoke(recipe.id)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(newInitialState, EditRecipeViewEffect.Delete)
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertValues(EditRecipeViewEvent.LoadingSave, EditRecipeViewEvent.OnSaveError)

        verify(deleteRecipesUseCase).invoke(recipe.id)
    }

    @Test
    fun `should return Recipe when invoke Delete and is successful`() {
        val recipeVM = TestHelper.getRecipeVM()
        val newInitialState = initialState.copyState(recipe = recipeVM)
        val expected = ActivityResult(
            newInitialState.recipe.copy(status = RecipeViewModel.Status.DELETED)
        )
        whenever(deleteRecipesUseCase.invoke(recipeVM.id)).thenReturn(Single.just(Unit))

        effectHandler.invoke(newInitialState, EditRecipeViewEffect.Delete)
            .cast(EditRecipeViewEvent::class.java)
            .test()
            .assertValue(EditRecipeViewEvent.LoadingSave)

        verify(deleteRecipesUseCase).invoke(recipeVM.id)
        verify(navigator).finishEditRecipe(expected)
    }
}
