package com.astutify.mealplanner.core.data

import com.astutify.mealplanner.core.domain.MeasurementLocalRepository
import com.astutify.mealplanner.core.domain.MeasurementRepository
import com.astutify.mealplanner.core.model.domain.Measurement
import io.reactivex.Single
import javax.inject.Inject

class MeasurementDataRepository @Inject constructor(
    private val apiRepository: MeasurementApiRepository,
    private val localRepository: MeasurementLocalRepository
) : MeasurementRepository {

    override fun getMeasurements(): Single<List<Measurement>> {
        val localMeasurements = localRepository.getMeasurements()
        return if (localMeasurements != null) {
            Single.just(localMeasurements)
        } else {
            apiRepository.getMeasurements()
                .map {
                    localRepository.storeMeasurements(it)
                    it
                }
        }
    }
}
