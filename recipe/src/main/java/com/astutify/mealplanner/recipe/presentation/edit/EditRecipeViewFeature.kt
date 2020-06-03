package com.astutify.mealplanner.recipe.presentation.edit

import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import javax.inject.Inject

class EditRecipeViewFeature @Inject constructor(
    recipe: RecipeViewModel?,
    reducer: EditRecipeViewReducer,
    effectHandler: EditRecipeViewEffectHandler
) : Feature<EditRecipeViewState, EditRecipeViewEvent, EditRecipeViewEffect>(
    initialState = recipe?.let {
        EditRecipeViewState(recipe = it, mode = EditRecipeViewState.Mode.EDIT)
    } ?: EditRecipeViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): EditRecipeViewEvent = EditRecipeViewEvent.LoadData
}
