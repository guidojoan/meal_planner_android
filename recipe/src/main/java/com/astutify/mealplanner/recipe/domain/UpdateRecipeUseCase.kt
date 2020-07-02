package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.core.model.domain.Recipe
import javax.inject.Inject

@Mockable
class UpdateRecipeUseCase @Inject constructor(
    private val api: RecipeRepository
) {
    operator fun invoke(recipe: Recipe) = api.updateRecipe(recipe)
}
