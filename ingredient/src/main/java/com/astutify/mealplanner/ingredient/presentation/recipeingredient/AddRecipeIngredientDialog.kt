package com.astutify.mealplanner.ingredient.presentation.recipeingredient

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.astutify.mealplanner.core.extension.getAsFloatNullable
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.databinding.DialogRecipeIngredientBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddRecipeIngredientDialog :
    DialogFragment() {

    private lateinit var view: DialogRecipeIngredientBinding
    val ingredient: IngredientViewModel by lazy {
        arguments?.getParcelable<IngredientViewModel>(INGREDIENT_EXTRA)!!
    }
    private lateinit var listener: AddRecipeIngredientDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.add_ingredient))
            .setPositiveButton(resources.getString(R.string.save), null)
            .setView(initView())
            .create()
    }

    private fun initView(): View {
        view = DialogRecipeIngredientBinding.inflate(LayoutInflater.from(context), null, false)
        view.measurementSelector.setListener {
            view.quantityLayout.suffixText = (it as MeasurementViewModel).getSuffix()
        }

        initMeasurementSelector()
        view.ingredientName.text = ingredient.name
        return view.root
    }

    private fun initMeasurementSelector() {
        val measurements = ingredient.measurements.map { it.measurement }
        view.measurementSelector.setOptions(measurements, measurements.first())
        view.quantityLayout.suffixText = measurements.first().getSuffix()
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
        val quantityValue = view.quantity.text?.getAsFloatNullable()
        if (quantityValue != null) {
            listener.onSelectRecipeIngredient(
                quantityValue,
                view.measurementSelector.getSelected() as MeasurementViewModel
            )
            dialog?.dismiss()
        } else {
            view.quantity.error = getString(R.string.error_quantity_not_set)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddRecipeIngredientDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement AddRecipeIngredientDialogListener"))
        }
    }

    interface AddRecipeIngredientDialogListener {
        fun onSelectRecipeIngredient(quantity: Float, measurement: MeasurementViewModel)
    }

    companion object {

        private const val INGREDIENT_EXTRA = "ingredientExtra"

        fun newInstance(ingredient: IngredientViewModel): AddRecipeIngredientDialog {
            return AddRecipeIngredientDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(INGREDIENT_EXTRA, ingredient)
                }
            }
        }
    }
}
