package com.astutify.mealplanner.recipe.presentation.edit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.astutify.mealplanner.recipe.R
import com.astutify.mealplanner.recipe.databinding.DialogAddIngredientGroupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddIngredientGroupDialog : DialogFragment() {

    private lateinit var listener: AddIngredientGroupDialogListener
    private lateinit var view: DialogAddIngredientGroupBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(context!!)
            .setTitle(resources.getString(R.string.add_section))
            .setPositiveButton(resources.getString(R.string.save), null)
            .setView(initView())
            .create()
    }

    private fun initView(): View {
        view = DialogAddIngredientGroupBinding.inflate(LayoutInflater.from(context), null, false)
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
        val nameValue = view.name.text.toString()
        if (nameValue.isNotBlank()) {
            dialog?.dismiss()
            listener.onAddIngredientGroup(nameValue)
        } else {
            view.name.error = getString(R.string.error_name_not_set)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddIngredientGroupDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement AddIngredientGroupDialogListener"))
        }
    }

    interface AddIngredientGroupDialogListener {
        fun onAddIngredientGroup(ingredientGroupName: String)
    }

    companion object {
        fun newInstance() = AddIngredientGroupDialog()
    }
}
