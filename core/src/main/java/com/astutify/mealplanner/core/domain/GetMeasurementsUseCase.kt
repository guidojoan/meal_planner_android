package com.astutify.mealplanner.core.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class GetMeasurementsUseCase @Inject constructor(
    private val measurementRepository: MeasurementRepository
) {
    operator fun invoke() = measurementRepository.getMeasurements()
}
