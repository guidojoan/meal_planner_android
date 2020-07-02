package com.astutify.mealplanner.coreui.model

import com.astutify.mealplanner.core.extension.EMPTY
import java.text.DecimalFormat
import java.util.Locale

class QuantityFormatter {

    companion object {
        fun formatQuantity(quantity: Float, measurement: MeasurementViewModel?): String {
            return when {
                quantity == 0f -> String.EMPTY
                quantity > 0f && quantity < 1f -> "${formatAsQuantity2(
                    quantity
                )}  ${getSuffix(
                    measurement
                )}"
                quantity == 1f -> "${formatAsQuantity(
                    quantity
                )}  ${getSuffix(
                    measurement
                )}"
                quantity > 1f && quantity < 1000f -> "${formatAsQuantity(
                    quantity
                )}  ${getSuffixPlural(
                    measurement
                )}"
                quantity == 1000f -> "${formatAsQuantity(
                    quantity / 1000
                )}   ${get1000Suffix(
                    measurement
                )}"
                else -> "${formatAsQuantity(
                    quantity / 1000
                )}   ${get1000SuffixPlural(
                    measurement
                )}"
            }
        }

        fun getSuffix(measurement: MeasurementViewModel?): String {
            return measurement?.let {
                when (getUnitSystem()) {
                    UnitSystem.IMPERIAL -> measurement.imperialSuffix
                    UnitSystem.METRIC -> measurement.metricSuffix
                }
            } ?: String.EMPTY
        }

        private fun get1000Suffix(measurement: MeasurementViewModel?): String {
            return measurement?.let {
                when (getUnitSystem()) {
                    UnitSystem.IMPERIAL ->
                        measurement.imperial1000Suffix?.let { it } ?: measurement.imperialSuffix
                    UnitSystem.METRIC ->
                        measurement.metric1000Suffix?.let { it } ?: measurement.metricSuffix
                }
            } ?: String.EMPTY
        }

        private fun getSuffixPlural(measurement: MeasurementViewModel?): String {
            return measurement?.let {
                when (getUnitSystem()) {
                    UnitSystem.IMPERIAL ->
                        measurement.imperialSuffixPlural?.let { it } ?: measurement.imperialSuffix
                    UnitSystem.METRIC ->
                        measurement.metricSuffixPlural?.let { it } ?: measurement.metricSuffix
                }
            } ?: String.EMPTY
        }

        private fun get1000SuffixPlural(measurement: MeasurementViewModel?): String {
            return measurement?.let {
                when (getUnitSystem()) {
                    UnitSystem.IMPERIAL ->
                        measurement.imperial1000SuffixPlural?.let { it }
                            ?: measurement.imperial1000Suffix?.let { it }
                            ?: measurement.imperialSuffix
                    UnitSystem.METRIC ->
                        measurement.metric1000SuffixPlural?.let { it }
                            ?: measurement.metric1000Suffix?.let { it }
                            ?: measurement.metricSuffix
                }
            } ?: String.EMPTY
        }

        private fun getUnitSystem(): UnitSystem {
            return when (Locale.getDefault().country) {
                "GB", "MM", "LR", "US" -> UnitSystem.IMPERIAL
                else -> UnitSystem.METRIC
            }
        }

        private fun formatAsQuantity2(quantity: Float): String {
            val formatter = DecimalFormat("0.0")
            return formatter.format(quantity)
        }

        private fun formatAsQuantity(quantity: Float): String {
            val formatter = DecimalFormat("#.#")
            return formatter.format(quantity)
        }

        private enum class UnitSystem {
            IMPERIAL, METRIC
        }
    }
}
