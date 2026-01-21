package com.alejandro.practicatipoexamen.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alejandro.practicatipoexamen.data.Carta
import com.alejandro.practicatipoexamen.databinding.ActivityCartasBinding
import com.alejandro.practicatipoexamen.helper.ConfirmDialog
import com.alejandro.practicatipoexamen.viewmodel.CartaViewModel

class CartasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartasBinding
    private val vistaModelo: CartaViewModel by viewModels()
    private lateinit var adaptador: ArrayAdapter<String>
    private val listaDatos = mutableListOf<String>()
    private val mapeoIdCartas = mutableListOf<Carta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDatos)
        binding.lvCartas.adapter = adaptador

        val cartasLiveData = vistaModelo.getAll()
        cartasLiveData.observe(this, Observer { tasks ->
            listaDatos.clear()
            mapeoIdCartas.clear()
            tasks.forEach {
                mapeoIdCartas.add(it)
                listaDatos.add("Destinatario: ${it.destinatario}\nContenido: ${it.contenido}\nRemitente: ${it.remitente}")
            }
            adaptador.notifyDataSetChanged()
        })

        val REQUEST_KEY_ELIMINAR_TODO = "ConfirmarEliminacionTodo"

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

        binding.btDeleteCartas.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_ELIMINAR_TODO,
                "¿Quieres eliminar todas las cartas?",
                "Esta acción no es reversible.",
                "Eliminar Todo",
                "Cancelar"
            ).show(supportFragmentManager, "EnviarDialogEliminacionTodo")
        }

        binding.lvCartas.setOnItemClickListener{_, _, posicion, _ ->
            val cartaBorrar = mapeoIdCartas[posicion]
            vistaModelo.delete(cartaBorrar)
            Toast.makeText(this, "Carta eliminada con éxito.", Toast.LENGTH_LONG)
                .show()
        }
    }

}