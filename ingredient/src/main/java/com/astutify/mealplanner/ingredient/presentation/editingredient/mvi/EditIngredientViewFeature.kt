package com.astutify.mealplanner.ingredient.presentation.editingredient.mvi

import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mvi.Feature
import javax.inject.Inject

class EditIngredientViewFeature @Inject constructor(
    ingredient: IngredientViewModel?,
    initialState: EditIngredientViewState?,
    reducer: EditIngredientViewReducer,
    effectHandler: EditIngredientViewEffectHandler
) : Feature<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect>(
    initialState = when {
        initialState != null -> initialState
        ingredient != null -> EditIngredientViewState(
            ingredient = ingredient,
            mode = EditIngredientViewState.Mode.EDIT
        )
        else -> EditIngredientViewState()
    },
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() =
        EditIngredientViewEvent.LoadData
}
