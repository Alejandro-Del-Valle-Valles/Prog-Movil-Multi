package com.alejandro.simulacroexamen01.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.simulacroexamen01.enums.TipoTropas
import com.alejandro.simulacroexamen01.data.Tropa
import com.alejandro.simulacroexamen01.databinding.ActivityConsultaBinding
import kotlin.random.Random

class ConsultaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultaBinding
    private val vistaModelo: VistaModeloTropa by viewModels()
    private lateinit var tropa: Tropa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vistaModelo.todasTropas.observe(this) { tropas ->
            if (tropas.isNotEmpty()) {
                tropa = tropas.random()
                recargarDatos()
            }
        }

        binding.btRecargar.setOnClickListener {
            vistaModelo.todasTropas.value?.let { tropas ->
                if (tropas.isNotEmpty()) {
                    tropa = tropas.random()
                    recargarDatos()
                }
            }
        }
    }

    private fun recargarDatos() {
        binding.tvDatos.text = tropa.toString()
    }
}