package com.astutify.mealplanner.recipe.domain

import com.astutify.mealplanner.core.model.domain.Recipe
import io.reactivex.Single

interface RecipeRepository {

    fun getRecipesFirstPage(keywords: String? = null): Single<List<Recipe>>

    fun getRecipesNextPage(): Single<List<Recipe>>

    fun saveRecipe(recipe: Recipe): Single<Recipe>

    fun updateRecipe(recipe: Recipe): Single<Recipe>

    fun deleteRecipe(recipeId: String): Single<Unit>
}
