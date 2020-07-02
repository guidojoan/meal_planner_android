package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.model.data.IngredientCategoryApi
import com.astutify.mealplanner.core.model.data.RecipeCategoryApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApi {

    @GET("ingredientCategory")
    fun getIngredientCategories(): Single<Response<List<IngredientCategoryApi>>>

    @GET("recipeCategory")
    fun getRecipeCategories(): Single<Response<List<RecipeCategoryApi>>>
}
