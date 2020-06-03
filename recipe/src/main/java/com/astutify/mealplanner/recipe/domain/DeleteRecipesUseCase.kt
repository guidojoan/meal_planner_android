package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class DeleteRecipesUseCase @Inject constructor(
    private val api: RecipeRepository
) {
    operator fun invoke(recipeId: String) = api.deleteRecipe(recipeId)
}
