package com.astutify.mealplanner.ingredient.domain

import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.core.entity.domain.Ingredient
import javax.inject.Inject

@Mockable
class UpdateIngredientUseCase @Inject constructor(
    private val api: IngredientRepository
) {
    operator fun invoke(ingredient: Ingredient) = api.updateIngredient(ingredient)
}
