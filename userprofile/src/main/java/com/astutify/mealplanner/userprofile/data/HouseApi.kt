package com.astutify.mealplanner.userprofile.data

import com.astutify.mealplanner.core.model.data.AboutApi
import com.astutify.mealplanner.core.model.data.HouseApi
import com.astutify.mealplanner.core.model.data.UserApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface HousesApi {

    @POST("house")
    fun postHouse(@Body house: HouseApi): Single<Response<Unit>>

    @GET("house")
    fun getHouse(): Single<Response<HouseApi>>

    @GET("house/search")
    fun searchHouse(@Query("keyword") keyword: String): Single<Response<List<HouseApi>>>

    @PUT("house/link")
    fun linkHouse(@Query("houseId") houseId: String, @Query("joinCode") joinCode: Int): Single<Response<Unit>>

    @PUT("house/unlink")
    fun unlinkHouse(): Single<Response<Unit>>

    @GET("/user")
    fun getUserProfile(): Single<Response<UserApi>>

    @GET("/about")
    fun getAbout(): Single<Response<AboutApi>>
}
