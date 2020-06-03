package com.astutify.mealplanner.core.authentication

import com.astutify.mealplanner.core.entity.data.AuthData
import com.astutify.mealplanner.core.entity.data.GoogleUser
import com.astutify.mealplanner.core.entity.data.UserEntity
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SessionApi {
    @POST("/refresh")
    fun refreshToken(@Body authData: AuthData): Call<AuthData>

    @POST("/login")
    fun login(@Body googleUser: GoogleUser): Single<Response<AuthData>>

    @POST("/logout")
    fun logout(@Body authData: AuthData): Single<Response<Unit>>

    @GET("/user")
    fun getUserProfile(): Single<Response<UserEntity>>
}
