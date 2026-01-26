package com.alejandro.examen02.app

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.alejandro.examen02.R
import com.alejandro.examen02.data.Vuelo
import com.alejandro.examen02.databinding.ActivityRegistroBinding
import com.alejandro.examen02.enums.TipoVuelo
import com.alejandro.examen02.helper.ConfirmDialog
import kotlin.getValue

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val vistaModelo: VistaModeloVuelo by viewModels()
    private lateinit var tipoVuelo: String
    private var numVuelo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tipoVuelo = intent.getStringExtra("tipo") !!

        val REQUEST_KEY_CODIGO = "ConfirmarRegreso"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODIGO, this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.btGuardar.setOnClickListener {
            guardarVuelos()
        }

        binding.btAdd.setOnClickListener {
            addRegistros()
        }

        binding.btVolver.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_CODIGO,
                "¿Estás seguro de que quieres volver?",
                "Los vuelos que no hayas guardado se borrarán",
                "Aceptar",
                "Cancelar",
            ).show(supportFragmentManager, "EnviarDialog")
        }
    }

    private fun guardarVuelos()
    {
        var tipo: TipoVuelo
        if(tipoVuelo == "NACIONAL") tipo = TipoVuelo.NACIONAL
        else if(tipoVuelo == "EUROPEO") tipo = TipoVuelo.EUROPEO
        else tipo = TipoVuelo.LARGA_DISTANCIA
        for(i in 0 until binding.LlVuelos.childCount step 3) {
            val origen = binding.LlVuelos.getChildAt(i + 1) as EditText
            val destino = binding.LlVuelos.getChildAt(i + 2) as EditText
            val nuevoVuelo = Vuelo(origen = origen.text.toString(),
                destino = destino.text.toString(), vuelo = tipo)
            vistaModelo.insert(nuevoVuelo)
        }
        val reporductor = MediaPlayer.create(this, R.raw.audio_examen)
        reporductor.start()
        reporductor.setOnCompletionListener { reporductor.release() }
        Toast.makeText(this, "Vuelos guardados correctamente", Toast.LENGTH_LONG)
            .show()
        binding.LlVuelos.removeAllViews()
    }


    private fun addRegistros() {
        numVuelo++
        val etOrigen = EditText(this)
        etOrigen.hint = "Origen del vuelo $numVuelo..."
        etOrigen.id = View.generateViewId()

        val etDestino = EditText(this)
        etDestino.hint = "Destino del vuelo $numVuelo..."
        etDestino.id = View.generateViewId()

        val etTipo = EditText(this)
        etTipo.setText(tipoVuelo)
        etTipo.isEnabled = false

        binding.LlVuelos.addView(etTipo)
        binding.LlVuelos.addView(etOrigen)
        binding.LlVuelos.addView(etDestino)
    }
}