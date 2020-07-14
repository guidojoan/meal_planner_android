package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.ingredient.presentation.Navigator
import com.astutify.mealplanner.ingredient.domain.GetIngredientsNexPageUseCase
import com.astutify.mealplanner.ingredient.domain.GetIngredientsUseCase
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi.RecipeIngredientsViewEffect
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi.RecipeIngredientsViewEffectHandler
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi.RecipeIngredientsViewEvent
import com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi.RecipeIngredientsViewState
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class RecipeIngredientsViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val getIngredientsUseCase: GetIngredientsUseCase = mock()
    private val getIngredientsNexPageUseCase: GetIngredientsNexPageUseCase = mock()
    private val effectHandler =
        RecipeIngredientsViewEffectHandler(
            Schedulers.trampoline(),
            navigator,
            getIngredientsUseCase,
            getIngredientsNexPageUseCase,
            INGREDIENT_GROUP_ID
        )
    private val initialState =
        RecipeIngredientsViewState()

    @Test
    fun `should go back if effect is ClickBack`() {
        effectHandler.invoke(initialState, RecipeIngredientsViewEffect.ClickBack)

        verify(navigator).goBack()
    }

    @Test
    fun `should navigate to add ingredient if effect is GoToAddIngredient`() {
        effectHandler.invoke(initialState, RecipeIngredientsViewEffect.GoToAddIngredient)

        verify(navigator).goToAddIngredient()
    }

    @Test
    fun `should finish add recipe ingredient if effect is IngredientQuantitySet`() {
        val captor = argumentCaptor<ActivityResult<RecipeIngredientViewModel>>()

        effectHandler.invoke(
            initialState,
            RecipeIngredientsViewEffect.IngredientQuantitySet(
                TestHelper.getIngredientVM(),
                QUANTITY,
                TestHelper.getPrimaryMeasurementVM()
            )
        )

        verify(navigator).finishAddRecipeIngredient(captor.capture())
        assert(captor.firstValue.result.ingredient == TestHelper.getIngredientVM())
        assert(captor.firstValue.result.quantity == QUANTITY)
        assert(captor.firstValue.result.measurement == TestHelper.getPrimaryMeasurementVM())
    }

    @Test
    fun `should not return anything if search keyword length is not ok`() {
        val searchKeyword = "To"

        effectHandler.invoke(initialState, RecipeIngredientsViewEffect.Search(searchKeyword))
            .test()
            .assertNoValues()
    }

    @Test
    fun `should return search results if search keyword length is correct`() {
        val searchKeyword = "Tomat"
        val expectedResult = listOf(TestHelper.getIngredientVM())
        whenever(getIngredientsUseCase(searchKeyword)).thenReturn(
            Single.just(
                listOf(
                    TestHelper.getIngredient()
                )
            )
        )

        val result =
            effectHandler.invoke(initialState, RecipeIngredientsViewEffect.Search(searchKeyword))
                .cast(RecipeIngredientsViewEvent::class.java)
                .test()
                .values()

        verify(getIngredientsUseCase)(searchKeyword)
        assert(result[0] == RecipeIngredientsViewEvent.Loading)
        assert((result[1] as RecipeIngredientsViewEvent.DataLoaded).ingredients == expectedResult)
    }

    @Test
    fun `should return search results when next page is invoked`() {
        val expectedResult = listOf(TestHelper.getIngredientVM())
        whenever(getIngredientsNexPageUseCase()).thenReturn(
            Single.just(
                listOf(
                    TestHelper.getIngredient()
                )
            )
        )

        val result = effectHandler.invoke(initialState, RecipeIngredientsViewEffect.LoadMoreData)
            .cast(RecipeIngredientsViewEvent::class.java)
            .test()
            .values()

        verify(getIngredientsNexPageUseCase)()
        assert(result[0] == RecipeIngredientsViewEvent.LoadingNext)
        assert((result[1] as RecipeIngredientsViewEvent.NextDataLoaded).ingredients == expectedResult)
    }

    @Test
    fun `should return error when next page is invoked and get error from API`() {
        whenever(getIngredientsNexPageUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, RecipeIngredientsViewEffect.LoadMoreData)
            .cast(RecipeIngredientsViewEvent::class.java)
            .test()
            .assertValues(
                RecipeIngredientsViewEvent.LoadingNext,
                RecipeIngredientsViewEvent.LoadingNextError
            )

        verify(getIngredientsNexPageUseCase)()
    }

    @Test
    fun `should return error if use case return an error`() {
        val searchKeyword = "Tomat"
        whenever(getIngredientsUseCase(searchKeyword)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, RecipeIngredientsViewEffect.Search(searchKeyword))
            .cast(RecipeIngredientsViewEvent::class.java)
            .test()
            .assertValues(
                RecipeIngredientsViewEvent.Loading,
                RecipeIngredientsViewEvent.LoadingError
            )

        verify(getIngredientsUseCase)(searchKeyword)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(navigator, getIngredientsUseCase, getIngredientsNexPageUseCase)
    }

    companion object {
        private const val INGREDIENT_GROUP_ID = "ingredientGroupId"
        private const val QUANTITY = 8f
    }
}
