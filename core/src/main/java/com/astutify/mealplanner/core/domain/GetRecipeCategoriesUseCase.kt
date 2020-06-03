package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class GetRecipeCategoriesUseCase @Inject constructor(
    private val api: CategoriesRepository
) {
    operator fun invoke() = api.getRecipeCategories()
}
