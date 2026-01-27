package com.alejandro.simulacroexamen01.app

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.alejandro.simulacroexamen01.R
import com.alejandro.simulacroexamen01.TipoTropas
import com.alejandro.simulacroexamen01.data.Tropa
import com.alejandro.simulacroexamen01.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var tipoTropa: String
    private var numTropa = 0
    private val vistaModelo: VistaModeloTropa by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("tipo").equals(TipoTropas.ZERG.name))
            tipoTropa = TipoTropas.ZERG.name
        else if(intent.getStringExtra("tipo").equals(TipoTropas.TERRAN.name))
            tipoTropa = TipoTropas.TERRAN.name
        else tipoTropa = TipoTropas.PROTOSS.name

        binding.tvTipoTropa.setText("Recluta a tropas de tipo $tipoTropa")

        binding.btAdd.setOnClickListener {
            addVistas()
        }

        binding.btGuardar.setOnClickListener {
            if(comprobarDatos()) {
                guardarTropas()
            } else Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btVolver.setOnClickListener {
            finish()
        }
    }

    private fun guardarTropas() {
        var player = MediaPlayer.create(this, R.raw.audio_examen)
        player.start()
        player.setOnCompletionListener { player.release() }
        for(i in 0 until binding.llTropas.childCount step 2) {
            val etNombre = binding.llTropas.getChildAt(i) as EditText
            val etFuerza = binding.llTropas.getChildAt(i + 1) as EditText
            val nuevaTropa = Tropa(nombre = etNombre.text.toString().trim(),
                fuerza = Integer.parseInt(etFuerza.text.toString().trim()),
                faccion = tipoTropa)
            vistaModelo.insert(nuevaTropa)
        }
        Toast.makeText(this, "Tropas guardadas correctamente", Toast.LENGTH_SHORT)
            .show()
        binding.llTropas.removeAllViews()
        numTropa = 0
    }

    private fun addVistas() {
        numTropa++
        val etNombre = EditText(this)
        etNombre.id = View.generateViewId()
        etNombre.hint = "Nombre de la tropa $numTropa"

        val etFuerza = EditText(this)
        etFuerza.id = View.generateViewId()
        etFuerza.inputType = EditorInfo.TYPE_CLASS_NUMBER
        etFuerza.hint = "Fuerza de la tropa $numTropa (Entre 1 y 100)"

        binding.llTropas.addView(etNombre)
        binding.llTropas.addView(etFuerza)
    }

    private fun comprobarDatos() : Boolean {
        for(i in 0 until binding.llTropas.childCount) {
            val etData = binding.llTropas.getChildAt(i) as EditText
            if(etData.text.isNullOrBlank()) return false
            if(etData.text.isDigitsOnly())  {
                val numero = Integer.parseInt(etData.text.toString())
                if(numero < 0 || numero > 100) return false
            }
        }
        return true
    }
}