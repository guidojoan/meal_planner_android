package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.entity.domain.IngredientCategory
import com.astutify.mealplanner.core.entity.domain.RecipeCategory
import io.reactivex.Single

interface CategoriesRepository {

    fun getIngredientCategories(): Single<List<IngredientCategory>>

    fun getRecipeCategories(): Single<List<RecipeCategory>>
}
