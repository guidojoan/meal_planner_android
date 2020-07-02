package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.model.domain.Measurement

class MeasurementLocalRepository {
    private var measurements: List<Measurement>? = null

    fun getMeasurements() = measurements

    fun storeMeasurements(measurements: List<Measurement>) {
        this.measurements = measurements
    }

    fun clearMeasurements() {
        measurements = null
    }
}
