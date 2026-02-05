package com.alejandro.repasorapidobbdd.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.alejandro.repasorapidobbdd.R
import com.alejandro.repasorapidobbdd.data.Alumno
import com.alejandro.repasorapidobbdd.databinding.ActivityConsultaBinding

class ConsultaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultaBinding
    private val vistaModelo: VistaModeloAlumno by viewModels()
    private lateinit var adaptador: ArrayAdapter<String>
    private val listaVisualizar = mutableListOf<String>()
    private val mapeoIdTarea = mutableListOf<Alumno>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            listaVisualizar)
        binding.lvAlumnos.adapter = adaptador
        val tareaLiveData = vistaModelo.todasTareas
        tareaLiveData.observe(this, Observer { tasks ->
            listaVisualizar.clear()
            mapeoIdTarea.clear()
            tasks.forEach {
                listaVisualizar.add(it.toString())
                mapeoIdTarea.add(it)
            }
            adaptador.notifyDataSetChanged()
        })

        binding.lvAlumnos.setOnItemClickListener { _, _, posicion, _ ->
            listaVisualizar.removeAt(posicion)
            var alumno = mapeoIdTarea.removeAt(posicion)
            vistaModelo.delete(alumno)
        }

        binding.btDelete.setOnClickListener {
            listaVisualizar.clear()
            mapeoIdTarea.clear()
            vistaModelo.deleteAll()
        }

        binding.btVolver.setOnClickListener {
            finish()
        }
    }
}