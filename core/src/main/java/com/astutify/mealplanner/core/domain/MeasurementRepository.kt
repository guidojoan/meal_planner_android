package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.model.domain.Measurement
import io.reactivex.Single

interface MeasurementRepository {

    fun getMeasurements(): Single<List<Measurement>>
}
