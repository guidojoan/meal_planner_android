package com.astutify.mealplanner.coreui.presentation

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.core.Mockable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

@Mockable
class AlertMessageBuilder @Inject constructor(
    private val context: AppCompatActivity
) {
    fun showAlert(
        title: String? = null,
        message: String,
        positiveButtonLabel: String,
        positiveAction: (DialogInterface) -> Unit,
        negativeButtonLabel: String? = null,
        negativeAction: ((DialogInterface) -> Unit)? = null
    ) {
        val builder = MaterialAlertDialogBuilder(context)

        builder.setPositiveButton(positiveButtonLabel) { dialog, _ ->
            positiveAction.invoke(dialog)
        }
        builder.setMessage(message)
        title?.let {
            builder.setTitle(it)
        }
        if (negativeButtonLabel != null && negativeAction != null) {
            builder.setNegativeButton(negativeButtonLabel) { dialog, _ ->
                negativeAction.invoke(dialog)
            }
        }

        builder
            .create()
            .show()
    }

    fun showSelectionAlert(
        title: String? = null,
        items: Array<String>,
        checkedItem: Int = 0,
        onClick: (Int) -> Unit
    ) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setSingleChoiceItems(items, checkedItem) { _, item ->
            onClick(item)
        }
        title?.let {
            builder.setTitle(it)
        }
        builder
            .create()
            .show()
    }
}
