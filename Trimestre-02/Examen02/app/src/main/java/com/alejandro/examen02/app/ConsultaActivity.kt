package com.alejandro.examen02.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.alejandro.examen02.R
import com.alejandro.examen02.data.Vuelo
import com.alejandro.examen02.databinding.ActivityConsultaBinding
import kotlin.getValue

class ConsultaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultaBinding
    private val vistaModelo: VistaModeloVuelo by viewModels()
    private lateinit var adapatador: ArrayAdapter<String>
    private val listaVisualizar = mutableListOf<String>()
    private val mapeoIdVuelo = mutableListOf<Vuelo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapatador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVisualizar)
        binding.lvVuelos.adapter = adapatador

        val vuelosLiveData = vistaModelo.getAll()
        vuelosLiveData.observe(this, Observer { task ->
            listaVisualizar.clear()
            mapeoIdVuelo.clear()
            task.forEach {
                listaVisualizar.add("${it.vuelo} | Origen: ${it.origen} | Destino: ${it.destino}")
                mapeoIdVuelo.add(it)
            }
            adapatador.notifyDataSetChanged()
        })

        binding.lvVuelos.setOnItemClickListener {_, _, posicion, _ ->
            val borrar = mapeoIdVuelo[posicion]
            vistaModelo.delete(borrar)
            Toast.makeText(this, "Vuelo borrado correctamente", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btVolverInicio.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}