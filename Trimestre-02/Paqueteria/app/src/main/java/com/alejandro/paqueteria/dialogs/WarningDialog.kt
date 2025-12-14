package com.alejandro.paqueteria.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WarningDialog: DialogFragment() {

    companion object {
        // --- Keys for the Argument ---
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_NEUTRAL_BTN = "ARG_NEUTRAL_BTN"

        // --- Keys for the Result ---
        const val RESULT_KEY_ACTION = "DIALOG_ACTION"
        const val ACTION_POSITIVE = "ACTION_POSITIVE"
        /**
         * Create a new Dialgo with personalized data
         *
         * @param title Dialog title
         * @param message Dialog message
         * @param buttonText Text for clos the dialog
         */
        fun newInstance(
            title: String,
            message: String,
            buttonText: String = "Ok",
        ): WarningDialog {
            val fragment = WarningDialog()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_NEUTRAL_BTN, buttonText)
            }
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Create the dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()

        val title = args.getString(ARG_TITLE)!!
        val message = args.getString(ARG_MESSAGE)!!
        val buttonText = args.getString(ARG_NEUTRAL_BTN)

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
        builder.setPositiveButton(buttonText) { _, _ ->
            val result = bundleOf(RESULT_KEY_ACTION to ACTION_POSITIVE)
            setFragmentResult("NoNeeded", result)
        }
        return builder.create()
    }

}