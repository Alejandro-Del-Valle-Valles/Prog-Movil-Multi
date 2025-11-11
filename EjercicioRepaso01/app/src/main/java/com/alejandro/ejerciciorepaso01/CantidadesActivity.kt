package com.alejandro.ejerciciorepaso01

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alejandro.ejerciciorepaso01.databinding.ActivityCantidadesBinding

class CantidadesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCantidadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCantidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDatosIntent()
        binding.btSalirCantidad.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            if(checkCantidadesIsNotNull()) {
                intentMain.putExtra("Cantidad 1", intent.getIntExtra("Cantidad 1", -1))
                intentMain.putExtra("Cantidad 2", intent.getIntExtra("Cantidad 2", -1))
                intentMain.putExtra("Cantidad 3", intent.getIntExtra("Cantidad 3", -1))
                intentMain.putExtra("Cantidad 4", intent.getIntExtra("Cantidad 4", -1))
                if(intent.getIntExtra("Cantidad 5", -1) != -1)
                    intentMain.putExtra("Cantidad 5", intent.getIntExtra("Cantidad 5", -1))

                intentMain.putExtra("Concepto 1", binding.tvConceptoUno.text.toString())
                intentMain.putExtra("Concepto 2", binding.tvConceptoDos.text.toString())
                intentMain.putExtra("Concepto 3", binding.tvConceptoTres.text.toString())
                intentMain.putExtra("Concepto 4", binding.tvConceptoCuatro.text.toString())
                intentMain.putExtra("Concepto 5", binding.tvConceptoCinco.text.toString())
            }
            startActivity(intentMain)
            finish()
        }

        binding.btAceptarCantidad.setOnClickListener {
            if(checkCantidadesIsNotNull()) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Cantidad 1", binding.etCantidadUno.text.toString().toIntOrNull())
                intent.putExtra("Cantidad 2", binding.etCantidadDos.text.toString().toIntOrNull())
                intent.putExtra("Cantidad 3", binding.etCantidadTres.text.toString().toIntOrNull())
                intent.putExtra("Cantidad 4", binding.etCantidadCuatro.text.toString().toIntOrNull())
                if(intent.getIntExtra("Cantidad 5", -1) != -1)
                    intent.putExtra("Cantidad 5", binding.etCantidadCinco.text.toString().toIntOrNull())

                intent.putExtra("Concepto 1", binding.tvConceptoUno.text.toString())
                intent.putExtra("Concepto 2", binding.tvConceptoDos.text.toString())
                intent.putExtra("Concepto 3", binding.tvConceptoTres.text.toString())
                intent.putExtra("Concepto 4", binding.tvConceptoCuatro.text.toString())
                intent.putExtra("Concepto 5", binding.tvConceptoCinco.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Necesitas rellenar al menos las 4 primeras cantidades.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkCantidadesIsNotNull(): Boolean {
        var esCorrecto = true
        if(binding.etCantidadUno.text.isNullOrBlank()) esCorrecto = false
        if(binding.etCantidadDos.text.isNullOrBlank()) esCorrecto = false
        if(binding.etCantidadTres.text.isNullOrBlank()) esCorrecto = false
        if(binding.etCantidadCuatro.text.isNullOrBlank()) esCorrecto = false
        if(!intent.getStringExtra("Concepto 5").isNullOrBlank()
            && binding.etCantidadCinco.text.isNullOrBlank() &&
            intent.getIntExtra("Cantidad 5", -1) != -1) esCorrecto = false
        return esCorrecto
    }

    private fun setDatosIntent() {
        binding.tvConceptoUno.text = intent.getStringExtra("Concepto 1")
        binding.tvConceptoDos.text = intent.getStringExtra("Concepto 2")
        binding.tvConceptoTres.text = intent.getStringExtra("Concepto 3")
        binding.tvConceptoCuatro.text = intent.getStringExtra("Concepto 4")
        if(intent.getStringExtra("Concepto 5").isNullOrBlank() ||
            intent.getIntExtra("Cantidad 5", -1) == -1) {
            binding.tvConceptoCinco.isVisible = false
            binding.etCantidadCinco.isVisible = false
        } else {
            binding.tvConceptoCinco.text = intent.getStringExtra("Concepto 5")
        }
        if(intent.getIntExtra("Cantidad 1", -1) != -1) {
            binding.etCantidadUno.setText(intent.getIntExtra("Cantidad 1", 0).toString())
        }
        if(intent.getIntExtra("Cantidad 2", -1) != -1) {
            binding.etCantidadDos.setText(intent.getIntExtra("Cantidad 2", 0).toString())
        }
        if(intent.getIntExtra("Cantidad 3", -1) != -1) {
            binding.etCantidadTres.setText(intent.getIntExtra("Cantidad 3", 0).toString())
        }
        if(intent.getIntExtra("Cantidad 4", -1) != -1) {
            binding.etCantidadCuatro.setText(intent.getIntExtra("Cantidad 4", 0).toString())
        }
        if(intent.getIntExtra("Cantidad 5", -1) != -1) {
            binding.etCantidadCinco.setText(intent.getIntExtra("Cantidad 5", 0).toString())
        }
    }

}