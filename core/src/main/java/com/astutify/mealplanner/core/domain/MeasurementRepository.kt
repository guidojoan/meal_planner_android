package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.entity.domain.Measurement
import io.reactivex.Single

interface MeasurementRepository {

    fun getMeasurements(): Single<List<Measurement>>
}
