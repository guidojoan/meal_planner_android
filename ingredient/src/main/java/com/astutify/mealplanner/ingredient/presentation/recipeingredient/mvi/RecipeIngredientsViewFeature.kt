package com.astutify.mealplanner.ingredient.presentation.recipeingredient.mvi

import com.astutify.mvi.Feature
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
