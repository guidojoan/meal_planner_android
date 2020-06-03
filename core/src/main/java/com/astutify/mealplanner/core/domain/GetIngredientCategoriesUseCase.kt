package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class GetIngredientCategoriesUseCase @Inject constructor(
    private val ingredientRepository: CategoriesRepository
) {
    operator fun invoke() = ingredientRepository.getIngredientCategories()
}
