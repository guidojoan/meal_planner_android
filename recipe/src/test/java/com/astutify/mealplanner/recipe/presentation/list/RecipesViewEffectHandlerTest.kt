package com.astutify.mealplanner.recipe.presentation.list

import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.coreui.presentation.TestHelper
import com.astutify.mealplanner.recipe.Navigator
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.recipe.domain.GetRecipesNextPageUseCase
import com.astutify.mealplanner.recipe.domain.GetRecipesUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class RecipesViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val outNavigator: RecipeOutNavigator = mock()
    private val getRecipesUseCase: GetRecipesUseCase = mock()
    private val getRecipesNextPageUseCase: GetRecipesNextPageUseCase = mock()
    private val sessionManager: SessionManager = mock()
    private val effectHandler = RecipesViewEffectHandler(
        Schedulers.trampoline(),
        navigator,
        outNavigator,
        getRecipesUseCase,
        getRecipesNextPageUseCase,
        sessionManager
    )
    private val initialState = RecipesViewState()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            navigator,
            outNavigator,
            getRecipesUseCase,
            getRecipesNextPageUseCase,
            sessionManager
        )
    }

    @Test
    fun `should return Load Event when is Logged and has house`() {
        whenever(sessionManager.isLogued()).thenReturn(true)
        whenever(sessionManager.hasHouse()).thenReturn(true)

        effectHandler.invoke(initialState, RecipesViewEffect.CheckLoginStatus)
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertValue(RecipesViewEvent.Load)

        verify(sessionManager).isLogued()
        verify(sessionManager).hasHouse()
    }

    @Test
    fun `should navigate to Login when not Logged`() {
        whenever(sessionManager.isLogued()).thenReturn(false)

        effectHandler.invoke(initialState, RecipesViewEffect.CheckLoginStatus)
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(sessionManager).isLogued()
        verify(outNavigator).goToLogin()
    }

    @Test
    fun `should navigate to House Edit when is Logged and not have House`() {
        whenever(sessionManager.isLogued()).thenReturn(true)
        whenever(sessionManager.hasHouse()).thenReturn(false)

        effectHandler.invoke(initialState, RecipesViewEffect.CheckLoginStatus)
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(sessionManager).isLogued()
        verify(sessionManager).hasHouse()
        verify(outNavigator).goToHouseEdit()
    }

    @Test
    fun `should navigate to AddRecipe when is invoked with GoToAddRecipe Effect`() {
        effectHandler.invoke(initialState, RecipesViewEffect.GoToAddRecipe)
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goToAddRecipe()
    }

    @Test
    fun `should navigate to RecipeDetail when is invoked with GoToAddRecipe GoToRecipeDetail`() {
        val recipe = TestHelper.getRecipeVM()

        effectHandler.invoke(initialState, RecipesViewEffect.GoToRecipeDetail(recipe))
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goToRecipeDetail(recipe)
    }

    @Test
    fun `should navigate to EditRecipe when is invoked with GoToEditRecipe Effect`() {
        val recipe = TestHelper.getRecipeVM()

        effectHandler.invoke(initialState, RecipesViewEffect.GoToEditRecipe(recipe))
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goToEditRecipe(recipe)
    }

    @Test
    fun `should get Event with Recipes when get recipes from backend`() {
        val recipes = listOf(TestHelper.getRecipe())
        val recipesVM = listOf(TestHelper.getRecipeVM())
        val keyword = "Pizza"
        whenever(getRecipesUseCase(keyword)).thenReturn(Single.just(recipes))

        val result = effectHandler.invoke(initialState, RecipesViewEffect.Search(keyword))
            .cast(RecipesViewEvent::class.java)
            .test()
            .values()

        assert((result[0] as RecipesViewEvent.DataLoaded).recipes == recipesVM)
        verify(getRecipesUseCase)(keyword)
    }

    @Test
    fun `should get Event with Recipes when get next page of recipes from backend`() {
        val recipes = listOf(TestHelper.getRecipe())
        val recipesVM = listOf(TestHelper.getRecipeVM())
        whenever(getRecipesNextPageUseCase()).thenReturn(Single.just(recipes))

        val result = effectHandler.invoke(initialState, RecipesViewEffect.EndOfListReached)
            .cast(RecipesViewEvent::class.java)
            .test()
            .values()

        assert(result[0] == RecipesViewEvent.LoadingNext)
        assert((result[1] as RecipesViewEvent.NextDataLoaded).recipes == recipesVM)
        verify(getRecipesNextPageUseCase)()
    }

    @Test
    fun `should get Event with error when get next page of recipes from backend`() {
        whenever(getRecipesNextPageUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, RecipesViewEffect.EndOfListReached)
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertValues(RecipesViewEvent.LoadingNext, RecipesViewEvent.LoadingNextError)

        verify(getRecipesNextPageUseCase)()
    }

    @Test
    fun `should not return anything when keyword size minor than 3`() {
        val keyword = "Pi"

        effectHandler.invoke(initialState, RecipesViewEffect.Search(keyword))
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()
    }

    @Test
    fun `should not return anything with get backend error`() {
        val keyword = "Pizza"
        whenever(getRecipesUseCase(keyword)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, RecipesViewEffect.Search(keyword))
            .cast(RecipesViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(getRecipesUseCase)(keyword)
    }
}
