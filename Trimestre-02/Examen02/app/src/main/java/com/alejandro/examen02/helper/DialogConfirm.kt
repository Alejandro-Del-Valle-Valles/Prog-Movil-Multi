package com.alejandro.examen02.helper

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ConfirmDialog : DialogFragment() {
    companion object {
        private const val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BTN = "ARG_POSITIVE_BTN"
        private const val ARG_NEGATIVE_BTN = "ARG_NEGATIVE_BTN"
        private const val ARG_NEUTRAL_BTN = "ARG_NEUTRAL_BTN"

        const val RESULT_KEY_ACTION = "DIALOG_ACTION"
        const val ACTION_POSITIVE = "ACTION_POSITIVE"
        const val ACTION_NEGATIVE = "ACTION_NEGATIVE"
        const val ACTION_NEUTRAL = "ACTION_NEUTRAL"

        fun newInstance(
            requestKey: String,
            title: String,
            message: String,
            positiveButtonText: String = "Aceptar",
            negativeButtonText: String? = null,
            neutralButtonText: String? = null
        ): ConfirmDialog {
            val fragment = ConfirmDialog()
            val args = Bundle().apply {
                putString(ARG_REQUEST_KEY, requestKey)
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_BTN, positiveButtonText)
                putString(ARG_NEGATIVE_BTN, negativeButtonText)
                putString(ARG_NEUTRAL_BTN, neutralButtonText)
            }
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val requestKey = args.getString(ARG_REQUEST_KEY)!!
        val title = args.getString(ARG_TITLE)!!
        val message = args.getString(ARG_MESSAGE)!!
        val positiveText = args.getString(ARG_POSITIVE_BTN)
        val negativeText = args.getString(ARG_NEGATIVE_BTN)
        val neutralText = args.getString(ARG_NEUTRAL_BTN)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
        builder.setPositiveButton(positiveText) { _, _ ->
            val result = bundleOf(RESULT_KEY_ACTION to ACTION_POSITIVE)
            setFragmentResult(requestKey, result)
        }
        if (negativeText != null) {
            builder.setNegativeButton(negativeText) { _, _ ->
                // Enviar resultado negativo
                val result = bundleOf(RESULT_KEY_ACTION to ACTION_NEGATIVE)
                setFragmentResult(requestKey, result)
            }
        }
        if (neutralText != null) {
            builder.setNeutralButton(neutralText) { _, _ ->
                val result = bundleOf(RESULT_KEY_ACTION to ACTION_NEUTRAL)
                setFragmentResult(requestKey, result)
            }
        }
        setCancelable(false)
        return builder.create()
    }
}
