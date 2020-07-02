package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.model.domain.IngredientCategory
import com.astutify.mealplanner.core.model.domain.RecipeCategory

class CategoriesLocalRepository {
    private var recipeCategories: List<RecipeCategory>? = null
    private var ingredientCategories: List<IngredientCategory>? = null

    fun getRecipeCategories() = recipeCategories

    fun getIngredientCategories() = ingredientCategories

    fun storeRecipeCategories(categories: List<RecipeCategory>) {
        this.recipeCategories = categories
    }

    fun storeIngredientCategories(categories: List<IngredientCategory>) {
        this.ingredientCategories = categories
    }
}
