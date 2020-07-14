package com.astutify.mealplanner.ingredient.presentation.list

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.ingredient.presentation.Navigator
import com.astutify.mealplanner.ingredient.domain.GetIngredientsNexPageUseCase
import com.astutify.mealplanner.ingredient.domain.GetIngredientsUseCase
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewEffect
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewEffectHandler
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewEvent
import com.astutify.mealplanner.ingredient.presentation.list.mvi.IngredientsViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class IngredientsViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val getIngredientsUseCase: GetIngredientsUseCase = mock()
    private val getIngredientsNexPageUseCase: GetIngredientsNexPageUseCase = mock()
    private val effectHandler =
        IngredientsViewEffectHandler(
            Schedulers.trampoline(),
            navigator,
            getIngredientsUseCase,
            getIngredientsNexPageUseCase
        )
    private val initialState =
        IngredientsViewState()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(navigator, getIngredientsUseCase)
    }

    @Test
    fun `should return Event with Ingredients when invoked with LoadData Effect`() {
        val ingredients = listOf(TestHelper.getIngredient())
        val ingredientsVM = listOf(TestHelper.getIngredientVM())
        whenever(getIngredientsUseCase()).thenReturn(Single.just(ingredients))

        val result = effectHandler.invoke(initialState, IngredientsViewEffect.LoadData)
            .cast(IngredientsViewEvent::class.java)
            .test()
            .values()

        assert(result[0] == IngredientsViewEvent.Loading)
        assert((result[1] as IngredientsViewEvent.DataLoaded).ingredients == ingredientsVM)
        verify(getIngredientsUseCase)()
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

        val result = effectHandler.invoke(initialState, IngredientsViewEffect.LoadMoreData)
            .cast(IngredientsViewEvent::class.java)
            .test()
            .values()

        verify(getIngredientsNexPageUseCase)()
        assert(result[0] == IngredientsViewEvent.LoadingNext)
        assert((result[1] as IngredientsViewEvent.NextDataLoaded).ingredients == expectedResult)
    }

    @Test
    fun `should return error when next page is invoked and get error from API`() {
        whenever(getIngredientsNexPageUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, IngredientsViewEffect.LoadMoreData)
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValues(IngredientsViewEvent.LoadingNext, IngredientsViewEvent.LoadingNextError)

        verify(getIngredientsNexPageUseCase)()
    }

    @Test
    fun `should return Error  when invoked with LoadData Effect and get error from Backend`() {
        whenever(getIngredientsUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, IngredientsViewEffect.LoadData)
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValues(IngredientsViewEvent.Loading, IngredientsViewEvent.LoadingError)

        verify(getIngredientsUseCase)()
    }

    @Test
    fun `should not return anything when invoked with Search Event and name is minor to 3`() {
        val keyword = "To"

        effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyword))
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertNoValues()
    }

    @Test
    fun `should not return anything when invoked with Search Event and get error from Backend`() {
        val keyword = "Tomato"
        whenever(getIngredientsUseCase(keyword)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyword))
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(getIngredientsUseCase)(keyword)
    }

    @Test
    fun `should not return Event with ingredients when invoked with Search Effect`() {
        val keyword = "Tomato"
        val ingredients = listOf(TestHelper.getIngredient())
        val ingredientsVM = listOf(TestHelper.getIngredientVM())
        whenever(getIngredientsUseCase(keyword)).thenReturn(Single.just(ingredients))

        effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyword))
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValue { result ->
                (result as IngredientsViewEvent.DataLoaded).ingredients == ingredientsVM
            }

        verify(getIngredientsUseCase)(keyword)
    }

    @Test
    fun `should navigate to AddIngredient when invoked with GoToAddIngredient Effect`() {
        effectHandler.invoke(initialState, IngredientsViewEffect.GoToAddIngredient)
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goToAddIngredient()
    }

    @Test
    fun `should navigate to EditIngredient when invoked with GoToEditIngredient Effect`() {
        val ingredient = TestHelper.getIngredientVM()
        effectHandler.invoke(initialState, IngredientsViewEffect.GoToEditIngredient(ingredient))
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertNoValues()

        verify(navigator).goToEditIngredient(ingredient)
    }
}
