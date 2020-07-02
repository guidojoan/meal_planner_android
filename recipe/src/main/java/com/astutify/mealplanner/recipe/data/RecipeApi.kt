package com.astutify.mealplanner.recipe.data

import com.astutify.mealplanner.core.model.PageEntity
import com.astutify.mealplanner.core.model.data.RecipeApi
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApi {

    @GET("recipe")
    fun getRecipes(
        @Query("keywords") keywords: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<Response<PageEntity<RecipeApi>>>

    @Multipart
    @POST("recipe")
    fun saveRecipe(
        @Part("recipe") recipe: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<Response<RecipeApi>>

    @Multipart
    @PUT("recipe")
    fun updateRecipe(
        @Part("recipe") recipe: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<Response<RecipeApi>>

    @DELETE("recipe/{id}")
    fun deleteRecipe(@Path("id") recipeId: String): Single<Response<Unit>>
}
