package com.astutify.mealplanner.recipe.presentation.detail

import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import javax.inject.Inject

class RecipeDetailViewFeature @Inject constructor(
    recipe: RecipeViewModel,
    reducer: RecipeDetailViewReducer,
    effectHandler: RecipeDetailViewEffectHandler
) : Feature<RecipeDetailViewState, RecipeDetailViewEvent, RecipeDetailViewEffect>(
    initialState = RecipeDetailViewState(recipe),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): RecipeDetailViewEvent? = null
}
