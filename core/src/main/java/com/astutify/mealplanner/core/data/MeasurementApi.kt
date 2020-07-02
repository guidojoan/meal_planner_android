package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.model.data.MeasurementApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface MeasurementApi {

    @GET("measurement")
    fun getMeasurements(): Single<Response<List<MeasurementApi>>>
}
