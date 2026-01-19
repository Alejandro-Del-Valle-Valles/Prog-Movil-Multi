package com.alejandro.practicatipoexamen.helper

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDialog : DialogFragment() {
    // El companion object contendrá la lógica de "fábrica"
    companion object {
        // --- Claves para los ARGUMENTOS (lo que entra) ---
        private const val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BTN = "ARG_POSITIVE_BTN"
        private const val ARG_NEGATIVE_BTN = "ARG_NEGATIVE_BTN"
        private const val ARG_NEUTRAL_BTN = "ARG_NEUTRAL_BTN"

        // --- Claves para el RESULTADO (lo que sale) ---
        const val RESULT_KEY_ACTION = "DIALOG_ACTION"
        const val ACTION_POSITIVE = "ACTION_POSITIVE"
        const val ACTION_NEGATIVE = "ACTION_NEGATIVE"
        const val ACTION_NEUTRAL = "ACTION_NEUTRAL"
        /**
         * Crea una nueva instancia de ConfirmDialog con parámetros personalizables.
         *
         * @param requestKey Clave ÚNICA para escuchar el resultado.
         * @param title El título del diálogo.
         * @param message El mensaje del diálogo.
         * @param positiveButtonText Texto para el botón positivo (ej: "Aceptar").
         * @param negativeButtonText Texto para el botón negativo (ej: "Cancelar"). (Opcional)
         * @param neutralButtonText Texto para el botón neutral (ej: "Más tarde"). (Opcional)
         */
        fun newInstance(
            requestKey: String,
            title: String,
            message: String,
            positiveButtonText: String = "Aceptar", // Valor por defecto
            negativeButtonText: String? = null, // Nulo si no se quiere
            neutralButtonText: String? = null // Nulo si no se quiere
        ): ConfirmDialog {
            // 1. Creamos la instancia del fragmento
            val fragment = ConfirmDialog()
            // 2. Creamos el "paquete" de argumentos
            val args = Bundle().apply {
                putString(ARG_REQUEST_KEY, requestKey)
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_BTN, positiveButtonText)
                putString(ARG_NEGATIVE_BTN, negativeButtonText)
                putString(ARG_NEUTRAL_BTN, neutralButtonText)
            }
            // 3. Asignamos los argumentos al fragmento
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Obtenemos los argumentos que pasamos en newInstance
        // requireArguments() se asegura de que no sean nulos
        val args = requireArguments()
        // Leemos los datos del Bundle
        val requestKey = args.getString(ARG_REQUEST_KEY)!!
        val title = args.getString(ARG_TITLE)!!
        val message = args.getString(ARG_MESSAGE)!!
        val positiveText = args.getString(ARG_POSITIVE_BTN)
        val negativeText = args.getString(ARG_NEGATIVE_BTN)
        val neutralText = args.getString(ARG_NEUTRAL_BTN)
        // Usamos el constructor de Material Design
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
        // --- Botón Positivo ---
        // Siempre hay un botón positivo
        builder.setPositiveButton(positiveText) { _, _ ->
            // Enviar resultado positivo
            val result = bundleOf(RESULT_KEY_ACTION to ACTION_POSITIVE)
            setFragmentResult(requestKey, result)
        }
        // --- Botón Negativo (Opcional) ---
        // Solo añadimos el botón si se proveyó un texto para él
        if (negativeText != null) {
            builder.setNegativeButton(negativeText) { _, _ ->
                // Enviar resultado negativo
                val result = bundleOf(RESULT_KEY_ACTION to ACTION_NEGATIVE)
                setFragmentResult(requestKey, result)
            }
        }
        // --- Botón Neutral (Opcional) ---
        // Solo añadimos el botón si se proveyó un texto para él
        if (neutralText != null) {
            builder.setNeutralButton(neutralText) { _, _ ->
                // Enviar resultado neutral
                val result = bundleOf(RESULT_KEY_ACTION to ACTION_NEUTRAL)
                setFragmentResult(requestKey, result)
            }
        }
        setCancelable(false)
        return builder.create()
    }
}