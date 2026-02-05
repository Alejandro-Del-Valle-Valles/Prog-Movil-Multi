package com.alejandro.simulacroexamen02.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import com.alejandro.simulacroexamen02.R
import com.alejandro.simulacroexamen02.data.Hechizo
import com.alejandro.simulacroexamen02.databinding.ActivityRegistroBinding
import kotlin.getValue

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val vistaModelo: VistaModeloHechizo by viewModels()
    private lateinit var tipoHechizo: String
    private var numeroRunas = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoHechizo = intent.getStringExtra("tipo")!!
        binding.etTipo.setText(tipoHechizo)

        binding.btVolver.setOnClickListener {
            finish()
        }

        binding.btGuardar.setOnClickListener {
            if(comprobarDatos()) {
                guardarHechizo()
            } else
                Toast.makeText(this, "Faltan datos. Debese rellenar todos los campos.",
                    Toast.LENGTH_SHORT).show()
        }

        binding.btAdd.setOnClickListener {
            addRuna()
        }
    }

    private fun addRuna() {
        numeroRunas++
        val etRuna = EditText(this)
        etRuna.hint = "Nombre la runa $numeroRunas..."

        binding.llRunas.addView(etRuna)
    }

    private fun guardarHechizo() {
        val nombre = binding.etNombre.text.toString().trim()
        val fuerza = binding.etFuerza.text.toString().trim().toInt()
        val numRunas = binding.llRunas.childCount
        val nuevoHechizo = Hechizo(tipo = tipoHechizo, nombre = nombre, dano = fuerza,
            numRunas = numRunas)
        vistaModelo.inserta(nuevoHechizo)
        binding.llRunas.removeAllViews()
        binding.etFuerza.text.clear()
        binding.etNombre.text.clear()
        val player = MediaPlayer.create(this, R.raw.audio_examen)
        player.start()
        player.setOnCompletionListener { player.release() }
        Toast.makeText(this, "Hechizo guardado correctamente", Toast.LENGTH_SHORT)
            .show()
        numeroRunas = 0
    }

    private fun comprobarDatos() : Boolean {
        if(binding.etNombre.text.isBlank()) return false
        if(binding.etFuerza.text.isBlank()) return false
        val fuerza = binding.etFuerza.text.toString().toInt()
        if(fuerza < 0 || fuerza > 100) return false
        binding.llRunas.forEach {
            val runa = it as EditText
            if(runa.text.isBlank()) return false
        }

        return true
    }
}