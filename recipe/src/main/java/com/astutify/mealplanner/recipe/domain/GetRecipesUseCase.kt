package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class GetRecipesUseCase @Inject constructor(
    private val api: RecipeRepository
) {
    operator fun invoke(keywords: String? = null) = api.getRecipesFirstPage(keywords)
}
