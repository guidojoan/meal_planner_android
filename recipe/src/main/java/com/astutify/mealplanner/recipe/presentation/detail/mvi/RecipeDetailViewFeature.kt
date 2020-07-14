package com.astutify.mealplanner.recipe.presentation.detail.mvi

import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mvi.Feature
import javax.inject.Inject

class RecipeDetailViewFeature @Inject constructor(
    recipe: RecipeViewModel,
    reducer: RecipeDetailViewReducer,
    effectHandler: RecipeDetailViewEffectHandler
) : Feature<RecipeDetailViewState, RecipeDetailViewEvent, RecipeDetailViewEffect>(
    initialState = RecipeDetailViewState(
        recipe
    ),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): RecipeDetailViewEvent? = null
}
