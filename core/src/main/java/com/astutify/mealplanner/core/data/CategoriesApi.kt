package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.entity.data.IngredientCategoryEntity
import com.astutify.mealplanner.core.entity.data.RecipeCategoryEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApi {

    @GET("ingredientCategory")
    fun getIngredientCategories(): Single<Response<List<IngredientCategoryEntity>>>

    @GET("recipeCategory")
    fun getRecipeCategories(): Single<Response<List<RecipeCategoryEntity>>>
}
