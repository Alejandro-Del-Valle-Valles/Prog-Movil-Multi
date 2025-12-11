package com.alejandro.testdebbdd.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alejandro.testdebbdd.data.Tarea
import com.alejandro.testdebbdd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: VistaModeloTarea by viewModels()
    private lateinit var adapter: ArrayAdapter<String>
    private val listaVisualizar = mutableListOf<String>()
    private val mapeoIdTarea = mutableListOf<Tarea>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, listaVisualizar)
        binding.lvTareas.adapter = adapter

        val tareaLiveData = viewModel.getAllTareasLiveData()
        tareaLiveData.observe(this, Observer {
            listaVisualizar.clear()
            mapeoIdTarea.clear()
            it.forEach { t ->
                listaVisualizar.add(t.titulo + if(t.completada) "Completado" else "")
                mapeoIdTarea.add(t)
            }
            adapter.notifyDataSetChanged()
        })

        binding.btAnadir.setOnClickListener {
            val text = binding.etTarea.text.toString().trim()
            if(text.isNotEmpty()) {
                val newTarea = Tarea(titulo = text)
                viewModel.insert(newTarea)
                binding.etTarea.text.clear()
                Toast.makeText(this, "Tarea añadida",
                    Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Introduce un texto.",
                Toast.LENGTH_SHORT).show()
        }

        binding.btBorraTodo.setOnClickListener {
            viewModel.deleteAll()
            Toast.makeText(this, "Tareas borradas", Toast.LENGTH_SHORT).show()
        }

        binding.btActualizarPrimero.setOnClickListener {
            if(mapeoIdTarea.isNotEmpty()) {
                val actTarea = mapeoIdTarea[0].copy(titulo = "Nuevo Título")
                viewModel.update(actTarea)
                Toast.makeText(
                    this, "Tarea actualizada ${actTarea.titulo}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.lvTareas.setOnItemClickListener { _, _, position, _ ->
            val tareaToDelete = mapeoIdTarea[position]
            viewModel.delete(tareaToDelete)
            Toast.makeText(this, "Tarea eliminada ${tareaToDelete.titulo}",
                Toast.LENGTH_SHORT).show()
        }
    }
}