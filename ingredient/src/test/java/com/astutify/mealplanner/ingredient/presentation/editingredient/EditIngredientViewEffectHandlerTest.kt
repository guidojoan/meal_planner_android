package com.astutify.mealplanner.ingredient.presentation.editingredient

import com.astutify.mealplanner.core.domain.GetIngredientCategoriesUseCase
import com.astutify.mealplanner.core.domain.GetMeasurementsUseCase
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.ingredient.Navigator
import com.astutify.mealplanner.ingredient.domain.AddIngredientUseCase
import com.astutify.mealplanner.ingredient.domain.UpdateIngredientUseCase
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class EditIngredientViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val addIngredientUseCase: AddIngredientUseCase = mock()
    private val getIngredientCategoriesUseCase: GetIngredientCategoriesUseCase = mock()
    private val getMeasurementsUseCase: GetMeasurementsUseCase = mock()
    private val updateIngredientUseCase: UpdateIngredientUseCase = mock()
    private val effectHandler = EditIngredientViewEffectHandler(
        Schedulers.trampoline(),
        navigator,
        addIngredientUseCase,
        getIngredientCategoriesUseCase,
        getMeasurementsUseCase,
        updateIngredientUseCase
    )
    private val initialState = EditIngredientViewState()

    @Test
    fun `should return State with Measurements,Categories when is invoked with LoadData Event`() {
        val categoryExpected = TestHelper.getIngredientCategoryVM()
        val measurementExpected = TestHelper.getPrimaryMeasurementVM()
        val customMeasurementExpected = TestHelper.getCustomMeasurementVM()
        val categories = listOf(TestHelper.getIngredientCategory())
        val measurements =
            listOf(TestHelper.getPrimaryMeasurement(), TestHelper.getCustomMeasurement())
        whenever(getIngredientCategoriesUseCase()).thenReturn(Single.just(categories))
        whenever(getMeasurementsUseCase()).thenReturn(Single.just(measurements))

        val result = effectHandler.invoke(initialState, EditIngredientViewEffect.LoadData)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .values()

        verify(getIngredientCategoriesUseCase)()
        verify(getMeasurementsUseCase)()
        assert(result[0] == EditIngredientViewEvent.Loading)
        assert((result[1] as EditIngredientViewEvent.DataLoaded).categories[0] == categoryExpected)
        assert((result[1] as EditIngredientViewEvent.DataLoaded).measurements[0] == measurementExpected)
        assert((result[1] as EditIngredientViewEvent.DataLoaded).customMeasurements[0] == customMeasurementExpected)
    }

    @Test
    fun `should return State with Error when is invoked with LoadData Event and get error from measurements api`() {
        val categories = listOf(TestHelper.getIngredientCategory())
        whenever(getIngredientCategoriesUseCase()).thenReturn(Single.just(categories))
        whenever(getMeasurementsUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, EditIngredientViewEffect.LoadData)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValues(EditIngredientViewEvent.Loading, EditIngredientViewEvent.LoadingError)

        verify(getIngredientCategoriesUseCase)()
        verify(getMeasurementsUseCase)()
    }

    @Test
    fun `should return State with Error when is invoked with LoadData Event and get error from categories api`() {
        whenever(getIngredientCategoriesUseCase()).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, EditIngredientViewEffect.LoadData)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValues(EditIngredientViewEvent.Loading, EditIngredientViewEvent.LoadingError)

        verify(getIngredientCategoriesUseCase)()
    }

    @Test
    fun `should navigate back when is invoked with GoBack Effect`() {
        effectHandler.invoke(initialState, EditIngredientViewEffect.GoBack)
            .test()
            .assertNoValues()

        verify(navigator).goBack()
    }

    @Test
    fun `should return State with Ingredient when is invoked with Save Effect`() {
        val ingredient = TestHelper.getIngredient()
        val ingredientVM = TestHelper.getIngredientVM()
        val initialState = initialState.copyState(ingredient = ingredientVM)
        val expectedResult = ActivityResult(
            ingredient.toPresentation().copy(status = IngredientViewModel.Status.NEW)
        )
        val argumentCaptor = argumentCaptor<ActivityResult<IngredientViewModel>>()
        whenever(addIngredientUseCase.invoke(ingredient)).thenReturn(Single.just(ingredient))

        effectHandler.invoke(initialState, EditIngredientViewEffect.Save)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValue(EditIngredientViewEvent.LoadingSave)

        verify(addIngredientUseCase)(ingredient)
        verify(navigator).finishAddIngredient(argumentCaptor.capture())
        assert(argumentCaptor.firstValue == expectedResult)
    }

    @Test
    fun `should return State with Ingredient when is invoked with Update Effect`() {
        val ingredient = TestHelper.getIngredient()
        val ingredientVM = TestHelper.getIngredientVM()
        val initialState = initialState.copyState(
            ingredient = ingredientVM,
            mode = EditIngredientViewState.Mode.EDIT
        )
        val expectedResult = ActivityResult(
            ingredient.toPresentation().copy(status = IngredientViewModel.Status.UPDATED)
        )
        val argumentCaptor = argumentCaptor<ActivityResult<IngredientViewModel>>()
        whenever(updateIngredientUseCase.invoke(ingredient)).thenReturn(Single.just(ingredient))

        effectHandler.invoke(initialState, EditIngredientViewEffect.Save)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValue(EditIngredientViewEvent.LoadingSave)

        verify(updateIngredientUseCase)(ingredient)
        verify(navigator).finishAddIngredient(argumentCaptor.capture())
        assert(argumentCaptor.firstValue == expectedResult)
    }

    @Test
    fun `should return Error when is invoked with Save Effect and get error from API`() {
        val ingredient = TestHelper.getIngredient()
        val ingredientVM = TestHelper.getIngredientVM()
        val initialState = initialState.copyState(ingredient = ingredientVM)
        whenever(addIngredientUseCase.invoke(ingredient)).thenReturn(Single.error(Throwable()))

        effectHandler.invoke(initialState, EditIngredientViewEffect.Save)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValues(EditIngredientViewEvent.LoadingSave, EditIngredientViewEvent.ErrorSave)

        verify(addIngredientUseCase)(ingredient)
    }

    @Test
    fun `should return ErrorNameTaken when is invoked with Save Effect and get error from API`() {
        val ingredient = TestHelper.getIngredient()
        val ingredientVM = TestHelper.getIngredientVM()
        val initialState = initialState.copyState(ingredient = ingredientVM)
        whenever(addIngredientUseCase.invoke(ingredient)).thenReturn(Single.error(TestHelper.getNameTakenError()))

        effectHandler.invoke(initialState, EditIngredientViewEffect.Save)
            .cast(EditIngredientViewEvent::class.java)
            .test()
            .assertValues(
                EditIngredientViewEvent.LoadingSave,
                EditIngredientViewEvent.ErrorNameTaken
            )

        verify(addIngredientUseCase)(ingredient)
    }
}
