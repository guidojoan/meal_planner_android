package com.astutify.mealplanner.coreui.model

import android.os.Parcelable
import com.astutify.mealplanner.core.extension.EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientViewModel(
    val id: String = String.EMPTY,
    val name: String = String.EMPTY,
    val measurements: List<IngredientMeasurementViewModel> = emptyList(),
    val category: IngredientCategoryViewModel = IngredientCategoryViewModel(),
    val packages: List<IngredientPackageViewModel>? = null,
    val status: Status = Status.SEEN
) : Parcelable {

    fun copyWithName(name: String) = copy(name = name)

    fun copyWithMeasurement(measurement: MeasurementViewModel) =
        copy(
            measurements = measurements.filterNot { it.measurement.primary }
                .toMutableList()
                .apply {
                    add(
                        IngredientMeasurementViewModel(
                            measurement = measurement
                        )
                    )
                }
        )

    fun copyWithCustomMeasurement(
        measurement: MeasurementViewModel,
        quantity: Float
    ): IngredientViewModel {
        return copy(
            measurements = measurements
                .toMutableList()
                .apply {
                    add(
                        IngredientMeasurementViewModel(
                            measurement = measurement,
                            quantity = quantity
                        )
                    )
                }
        )
    }

    fun copyWithOutCustomMeasurement(customMeasurementId: String): IngredientViewModel {
        return copy(measurements = measurements.filterNot { it.measurement.id == customMeasurementId })
    }

    fun copyWithCategory(category: IngredientCategoryViewModel) = copy(category = category)

    fun copyWithPackage(newPackage: IngredientPackageViewModel): IngredientViewModel {
        val packagesMutable: List<IngredientPackageViewModel>
        if (packages == null) {
            packagesMutable = mutableListOf()
            packagesMutable.add(newPackage)
        } else {
            packagesMutable = packages.toMutableList()
            packagesMutable.add(newPackage)
        }
        return copy(packages = packagesMutable)
    }

    fun copyWitOutPackage(packageId: String): IngredientViewModel {
        return copy(packages = packages?.filterNot { it.id == packageId })
    }

    fun getPrimaryMeasurement() =
        measurements.find { it.measurement.primary }?.measurement

    fun getCustomMeasurements() =
        measurements.filterNot { it.measurement.primary }

    private fun hasPrimaryMeasurement() =
        getPrimaryMeasurement()?.let { true } ?: false

    fun copyWithSeenStatus() = copy(status = Status.SEEN)

    fun isReadyToSave() =
        name.isNotBlank() && hasPrimaryMeasurement() && category.id.isNotBlank()

    enum class Status {
        NEW, UPDATED, SEEN
    }
}
