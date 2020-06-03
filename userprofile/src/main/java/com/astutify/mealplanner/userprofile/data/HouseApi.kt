package com.astutify.mealplanner.userprofile.data

import com.astutify.mealplanner.core.entity.data.AboutEntity
import com.astutify.mealplanner.core.entity.data.HouseEntity
import com.astutify.mealplanner.core.entity.data.UserEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface HouseApi {

    @POST("house")
    fun postHouse(@Body house: HouseEntity): Single<Response<Unit>>

    @GET("house")
    fun getHouse(): Single<Response<HouseEntity>>

    @GET("house/search")
    fun searchHouse(@Query("keyword") keyword: String): Single<Response<List<HouseEntity>>>

    @PUT("house/link")
    fun linkHouse(@Query("houseId") houseId: String, @Query("joinCode") joinCode: Int): Single<Response<Unit>>

    @PUT("house/unlink")
    fun unlinkHouse(): Single<Response<Unit>>

    @GET("/user")
    fun getUserProfile(): Single<Response<UserEntity>>

    @GET("/about")
    fun getAbout(): Single<Response<AboutEntity>>
}
