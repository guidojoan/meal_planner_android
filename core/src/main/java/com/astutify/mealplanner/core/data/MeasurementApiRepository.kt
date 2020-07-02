package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.model.data.error.ApiErrorManager
import com.astutify.mealplanner.core.model.data.mapper.toDomain
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class MeasurementApiRepository @Inject constructor(
    private val api: MeasurementApi,
    private val apiErrorManager: ApiErrorManager
) {

    fun getMeasurements() = api.getMeasurements()
        .flatMap {
            if (it.isSuccessful) {
                just(
                    it.body()!!.let { measurements ->
                        measurements.map { item ->
                            item.toDomain()
                        }
                    }
                )
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }
}
