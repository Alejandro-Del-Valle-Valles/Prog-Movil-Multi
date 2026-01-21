package com.alejandro.practicatipoexamen.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.practicatipoexamen.R
import com.alejandro.practicatipoexamen.databinding.ActivityCartasBinding
import com.alejandro.practicatipoexamen.helper.ConfirmDialog
import com.alejandro.practicatipoexamen.viewmodel.CartaViewModel

class CartasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartasBinding
    private val vistaModelo: CartaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val REQUEST_KEY_ELIMINAR_TODO = "ConfirmarEliminacionTodo"
        val REQUEST_KEY_ELIMINAR_CARTA = "ConfirmarEliminacion"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_ELIMINAR_TODO, this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    vistaModelo.deleteAll()
                    Toast.makeText(this, "Cartas borradas", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_ELIMINAR_CARTA, this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    //TODO: Eliminar Carta pulsada
                    Toast.makeText(this, "Carta borrada", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding = ActivityCartasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btDeleteCartas.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_ELIMINAR_TODO,
                "¿Quieres eliminar todas las cartas?",
                "Esta acción no es reversible.",
                "Eliminar Todo",
                "Cancelar"
            ).show(supportFragmentManager, "EnviarDialogEliminacionTodo")
        }
    }


}