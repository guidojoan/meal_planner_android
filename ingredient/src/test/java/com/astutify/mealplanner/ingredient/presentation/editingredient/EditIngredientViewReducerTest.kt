package com.astutify.mealplanner.ingredient.presentation.editingredient

import com.astutify.mealplanner.coreui.presentation.utils.TestHelper
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewEffect
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewEvent
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewReducer
import com.astutify.mealplanner.ingredient.presentation.editingredient.mvi.EditIngredientViewState
import org.junit.Test

class EditIngredientViewReducerTest {

    private val reducer =
        EditIngredientViewReducer()
    private val initialState =
        EditIngredientViewState()

    @Test
    fun `should return LoadData Effect when is invoked with LoadData Event`() {
        val result = reducer.invoke(initialState, EditIngredientViewEvent.LoadData)

        assert(result.effect is EditIngredientViewEffect.LoadData)
    }

    @Test
    fun `should return Save Effect when is invoked with LoadData Event`() {
        val result = reducer.invoke(initialState, EditIngredientViewEvent.Save)

        assert(result.effect is EditIngredientViewEffect.Save)
    }

    @Test
    fun `should return Loading State when is invoked with LoadingSave Event`() {
        val expectedResult = initialState.copyState(
            loading = EditIngredientViewState.Loading.LOADING_SAVE
        )
        val result = reducer.invoke(initialState, EditIngredientViewEvent.LoadingSave)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return State with categories and measurements when is invoked with DataLoaded Event`() {
        val event = EditIngredientViewEvent.DataLoaded(
            listOf(TestHelper.getCustomMeasurementVM()),
            listOf(TestHelper.getPrimaryMeasurementVM()),
            listOf(TestHelper.getIngredientCategoryVM())
        )
        val expectedResult = initialState.copyState(
            measurements = event.measurements,
            customMeasurements = event.customMeasurements,
            categories = event.categories
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return LoadingError State when is invoked with LoadingError Event`() {
        val expectedResult = initialState.copyState(
            error = EditIngredientViewState.Error.ERROR_LOADING
        )
        val result = reducer.invoke(initialState, EditIngredientViewEvent.LoadingError)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return ErrorSave State when is invoked with ErrorSave Event`() {
        val expectedResult = initialState.copyState(
            error = EditIngredientViewState.Error.ERROR_SAVE
        )
        val result = reducer.invoke(initialState, EditIngredientViewEvent.ErrorSave)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return State with Measurement when is invoked with MeasurementSelected Event`() {
        val measurement = TestHelper.getPrimaryMeasurementVM()
        val event = EditIngredientViewEvent.MeasurementSelected(measurement)

        val result = reducer.invoke(initialState, event)

        assert(result.state!!.ingredient.measurements[0].measurement == measurement)
    }

    @Test
    fun `should return State with Category when is invoked with CategorySelected Event`() {
        val category = TestHelper.getIngredientCategoryVM()
        val event = EditIngredientViewEvent.CategorySelected(category)
        val expectedResult = initialState.copyState(
            ingredient = initialState.ingredient.copyWithCategory(event.category)
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return State with Name when is invoked with NameChanged Event`() {
        val name = "name"
        val event = EditIngredientViewEvent.NameChanged(name)
        val expectedResult = initialState.copyState(
            ingredient = initialState.ingredient.copyWithName(event.name)
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return State with Package when is invoked with PackageAdded Event`() {
        val ingredientPackage = TestHelper.getPackageVM()
        val event = EditIngredientViewEvent.PackageAdded("packageName", 80f)
        val expectedResult = initialState.copyState(
            ingredient = initialState.ingredient.copyWithPackage(ingredientPackage)
        )

        val result = reducer.invoke(initialState, event)

        assert(result.state!!.ingredient.packages!![0].name == expectedResult.ingredient.packages!![0].name)
        assert(result.state!!.ingredient.packages!![0].quantity == expectedResult.ingredient.packages!![0].quantity)
    }

    @Test
    fun `should return State with --Package when is invoked with PackageRemoved Event`() {
        val ingredientPackage = TestHelper.getPackageVM()
        val initialState = initialState.copyState(
            ingredient = initialState.ingredient.copyWithPackage(ingredientPackage)
        )
        val event = EditIngredientViewEvent.PackageRemoved(ingredientPackage.id)
        val expectedResult = initialState.copyState(
            ingredient = initialState.ingredient.copyWitOutPackage(ingredientPackage.id)
        )

        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedResult)
    }

    @Test
    fun `should return GoBack Effect when is invoked with ClickBack Event`() {
        val result = reducer.invoke(initialState, EditIngredientViewEvent.ClickBack)

        assert(result.effect is EditIngredientViewEffect.GoBack)
    }

    @Test
    fun `should return ErrorNameTaken State when is invoked with ErrorNameTaken Event`() {
        val expectedState =
            initialState.copyState(error = EditIngredientViewState.Error.ERROR_NAME_TAKEN)

        val result = reducer.invoke(initialState, EditIngredientViewEvent.ErrorNameTaken)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return Add Custom Measurement State when is invoked with ClickAddCustomMeasurement Event`() {
        val expectedState =
            initialState.copyState(dialog = EditIngredientViewState.Dialog.ADD_CUSTOM_MEASUREMENT)

        val result = reducer.invoke(initialState, EditIngredientViewEvent.ClickAddCustomMeasurement)

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with ++Custom Measurement when is invoked with CustomMeasurementAdded Event`() {
        val measurementVM = TestHelper.getCustomMeasurementVM()
        val quantity = 80f
        val initialState = initialState.copyState(
            customMeasurements = listOf(
                TestHelper.getCustomMeasurementVM(),
                TestHelper.getPrimaryMeasurementVM()
            )
        )

        val result = reducer.invoke(
            initialState,
            EditIngredientViewEvent.CustomMeasurementAdded(measurementVM, quantity)
        )

        assert(result.state!!.ingredient.measurements[0].measurement == measurementVM)
        assert(result.state!!.ingredient.measurements[0].quantity == quantity)
        assert(result.state!!.customMeasurements.size == 1)
    }
}
