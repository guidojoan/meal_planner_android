package com.astutify.mealplanner.ingredient.presentation.editingredient

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.astutify.mealplanner.core.extension.getAsFloat
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.databinding.DialogAddCustomMeasurementBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddCustomMeasurementDialog : DialogFragment() {

    private lateinit var view: DialogAddCustomMeasurementBinding
    private lateinit var listener: AddCustomMeasurementDialogListener
    private val measurements: ArrayList<MeasurementViewModel> by lazy {
        arguments?.getParcelableArrayList<MeasurementViewModel>(MEASUREMENTS_EXTRA)!!
    }
    private val primaryMeasurement: MeasurementViewModel? by lazy {
        arguments?.getParcelable<MeasurementViewModel>(PRIMARY_MEASUREMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(context!!)
            .setTitle(resources.getString(R.string.add_custom_measurement))
            .setPositiveButton(resources.getString(R.string.save), null)
            .setView(initView())
            .create()
    }

    private fun initView(): View {
        view = DialogAddCustomMeasurementBinding.inflate(LayoutInflater.from(context), null, false)
        initMeasurements()
        return view.root
    }

    private fun initMeasurements() {
        primaryMeasurement?.let { view.quantityLayout.suffixText = it.getSuffix() }
        view.measurementSelector.setOptions(measurements, measurements.first())
    }

    override fun onResume() {
        super.onResume()
        (dialog as AlertDialog?)?.let {
            val button = it.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener { validateInput() }
        }
    }

    private fun validateInput() {
        view.quantity.error = null
        if (validateQuantity()) {
            dialog?.dismiss()
            listener.addCustomMeasurement(
                getSelectedMeasurement(),
                getQuantityValue()
            )
        }
    }

    private fun getSelectedMeasurement(): MeasurementViewModel {
        return view.measurementSelector.getSelected() as MeasurementViewModel
    }

    private fun getQuantityValue() = view.quantity.text!!.getAsFloat()

    private fun validateQuantity(): Boolean {
        val quantityValue = view.quantity.text?.getAsFloat()
        return if (quantityValue != null && quantityValue != 0f) {
            true
        } else {
            view.quantity.error = getString(R.string.error_quantity_not_set)
            false
        }
    }

    interface AddCustomMeasurementDialogListener {
        fun addCustomMeasurement(measurement: MeasurementViewModel, quantity: Float)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddCustomMeasurementDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement AddCustomMeasurementDialogListener"))
        }
    }

    companion object {

        private const val MEASUREMENTS_EXTRA = "measurementsExtra"
        private const val PRIMARY_MEASUREMENT = "primaryMeasurement"

        fun newInstance(
            measurements: ArrayList<MeasurementViewModel>,
            primaryMeasurement: MeasurementViewModel?
        ) = AddCustomMeasurementDialog().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(MEASUREMENTS_EXTRA, measurements)
                primaryMeasurement?.let { putParcelable(PRIMARY_MEASUREMENT, primaryMeasurement) }
            }
        }
    }
}
