package com.astutify.mealplanner.ingredient.data

import com.astutify.mealplanner.core.model.PageEntity
import com.astutify.mealplanner.core.model.data.IngredientApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface IngredientsApi {

    @POST("ingredient")
    fun saveIngredient(@Body ingredient: IngredientApi): Single<Response<IngredientApi>>

    @PUT("ingredient")
    fun updateIngredient(@Body ingredient: IngredientApi): Single<Response<IngredientApi>>

    @GET("ingredient")
    fun getIngredients(
        @Query("keywords") keywords: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<Response<PageEntity<IngredientApi>>>
}
