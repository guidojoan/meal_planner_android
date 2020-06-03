package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import javax.inject.Inject

class RecipeIngredientsViewFeature @Inject constructor(
    reducer: RecipeIngredientsViewReducer,
    effectHandler: RecipeIngredientsViewEffectHandler
) : Feature<RecipeIngredientsViewState, RecipeIngredientsViewEvent, RecipeIngredientsViewEffect>(
    initialState = RecipeIngredientsViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent(): RecipeIngredientsViewEvent? = null
}
