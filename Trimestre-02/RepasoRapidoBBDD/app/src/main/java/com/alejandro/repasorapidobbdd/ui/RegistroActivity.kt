package com.alejandro.repasorapidobbdd.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.repasorapidobbdd.R
import com.alejandro.repasorapidobbdd.data.Alumno
import com.alejandro.repasorapidobbdd.databinding.ActivityRegistroBinding
import kotlin.getValue

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val vistaModelo: VistaModeloAlumno by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btInicio.setOnClickListener {
            finish()
        }

        binding.btGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val edad =binding.etEdad.text.toString().trim().toInt()
            val alumnoNuevo = Alumno(nombre = nombre, edad = edad)
            vistaModelo.insert(alumnoNuevo)
            binding.etEdad.text.clear()
            binding.etNombre.text.clear()
        }
    }
}