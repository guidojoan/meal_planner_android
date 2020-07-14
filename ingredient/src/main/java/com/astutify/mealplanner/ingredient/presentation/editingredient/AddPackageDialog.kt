package com.astutify.mealplanner.ingredient.presentation.editingredient

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.astutify.mealplanner.core.extension.getAsFloat
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.databinding.DialogAddPackageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddPackageDialog : DialogFragment() {

    private lateinit var view: DialogAddPackageBinding
    private lateinit var listener: AddPackageDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(context!!)
            .setTitle(resources.getString(R.string.add_presentation))
            .setPositiveButton(resources.getString(R.string.save), null)
            .setView(initView())
            .create()
    }

    private fun initView(): View {
        view = DialogAddPackageBinding.inflate(LayoutInflater.from(context), null, false)
        return view.root
    }

    override fun onResume() {
        super.onResume()
        (dialog as AlertDialog?)?.let {
            val button = it.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener { validateInput() }
        }
    }

    private fun validateInput() {
        view.name.error = null
        view.quantity.error = null
        if (validateQuantity() && validateName()) {
            dialog?.dismiss()
            listener.onAddPackage(getNameValue(), getQuantityValue())
        }
    }

    private fun getNameValue() = view.name.text.toString()
    private fun getQuantityValue() = view.quantity.text!!.getAsFloat()

    private fun validateName(): Boolean {
        return if (view.name.text.toString().isNotBlank()) {
            true
        } else {
            view.name.error = getString(R.string.error_package_name_not_set)
            false
        }
    }

    private fun validateQuantity(): Boolean {
        val quantityValue = view.quantity.text?.getAsFloat()
        return if (quantityValue != null && quantityValue != 0f) {
            true
        } else {
            view.quantity.error = getString(R.string.error_quantity_not_set)
            false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddPackageDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement AddPackageDialogListener"))
        }
    }

    interface AddPackageDialogListener {
        fun onAddPackage(name: String, quantity: Float)
    }

    companion object {
        fun newInstance() = AddPackageDialog()
    }
}
