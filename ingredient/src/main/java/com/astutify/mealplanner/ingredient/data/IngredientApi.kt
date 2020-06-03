package com.astutify.mealplanner.ingredient.data

import com.astutify.mealplanner.core.entity.PageEntity
import com.astutify.mealplanner.core.entity.data.IngredientEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface IngredientApi {

    @POST("ingredient")
    fun saveIngredient(@Body ingredient: IngredientEntity): Single<Response<IngredientEntity>>

    @PUT("ingredient")
    fun updateIngredient(@Body ingredient: IngredientEntity): Single<Response<IngredientEntity>>

    @GET("ingredient")
    fun getIngredients(@Query("keywords") keywords: String?, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<Response<PageEntity<IngredientEntity>>>
}
