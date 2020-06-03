package com.astutify.mealplanner.ingredient.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class GetIngredientsUseCase @Inject constructor(
    private val api: IngredientRepository
) {
    operator fun invoke(keyWords: String? = null) =
        api.getIngredientsFirstPage(keyWords)
}
