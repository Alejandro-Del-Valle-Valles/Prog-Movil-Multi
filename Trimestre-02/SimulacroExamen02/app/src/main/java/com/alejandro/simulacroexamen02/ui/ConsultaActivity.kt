package com.alejandro.simulacroexamen02.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.simulacroexamen02.data.Hechizo
import com.alejandro.simulacroexamen02.databinding.ActivityConsultaBinding
import kotlin.getValue

class ConsultaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultaBinding
    private val vistaModelo: VistaModeloHechizo by viewModels()
    private lateinit var hechizo: Hechizo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vistaModelo.todosHechizo.observe(this) {hechizos ->
            if(hechizos.isNotEmpty()) {
                hechizo = hechizos.random() //Comprobamos en el main que no está vacía la BBDD
                binding.tvHechizo.setText(hechizo.toString())
                refrescarInformacion()
            }
        }

        binding.btLanzar.setOnClickListener {
            vistaModelo.todosHechizo.value?.let { hechizos ->
                if(hechizos.isNotEmpty()) {
                    hechizo = hechizos.random()
                    refrescarInformacion()
                }
            }
        }
    }

    private fun refrescarInformacion() {
        binding.tvHechizo.setText(hechizo.toString())
        binding.cvDibujo.tipo = hechizo.tipo
        binding.cvDibujo.size = hechizo.numRunas
    }
}