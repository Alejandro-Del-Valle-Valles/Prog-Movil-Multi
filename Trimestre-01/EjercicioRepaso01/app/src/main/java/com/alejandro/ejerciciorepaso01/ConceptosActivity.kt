package com.alejandro.ejerciciorepaso01

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.ejerciciorepaso01.databinding.ActivityConceptosBinding

class ConceptosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConceptosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConceptosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDatosIntent()
        binding.btSalirConceptos.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            if(checkConceptosIsNotNull()) {
                intentMain.putExtra("Concepto 1", intent.getStringExtra("Concepto 1"))
                intentMain.putExtra("Concepto 2", intent.getStringExtra("Concepto 2"))
                intentMain.putExtra("Concepto 3", intent.getStringExtra("Concepto 3"))
                intentMain.putExtra("Concepto 4", intent.getStringExtra("Concepto 4"))
                intentMain.putExtra("Concepto 5", intent.getStringExtra("Concepto 5"))
            }
            startActivity(intentMain)
            finish()
        }

        binding.btAceptarConceptos.setOnClickListener {
            if(checkConceptosIsNotNull()) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Concepto 1", binding.etConceptoUno.text.toString())
                intent.putExtra("Concepto 2", binding.etConceptoDos.text.toString())
                intent.putExtra("Concepto 3", binding.etConceptoTres.text.toString())
                intent.putExtra("Concepto 4", binding.etConceptoCuatro.text.toString())
                intent.putExtra("Concepto 5", binding.etConceptoCinco.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,
                    "Tienes que rellenar almenos los 4 primeros conceptos",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkConceptosIsNotNull(): Boolean {
        var esCorrecto = true
        if(binding.etConceptoUno.text.isNullOrBlank()) esCorrecto = false
        if(binding.etConceptoDos.text.isNullOrBlank()) esCorrecto = false
        if(binding.etConceptoTres.text.isNullOrBlank()) esCorrecto = false
        if(binding.etConceptoCuatro.text.isNullOrBlank()) esCorrecto = false
        return esCorrecto
    }

    private fun setDatosIntent() {
        if(!intent.getStringExtra("Concepto 1").isNullOrBlank()) {
            binding.etConceptoUno.setText(intent.getStringExtra("Concepto 1"))
        }
        if(!intent.getStringExtra("Concepto 2").isNullOrBlank()) {
            binding.etConceptoDos.setText(intent.getStringExtra("Concepto 2"))
        }
        if(!intent.getStringExtra("Concepto 3").isNullOrBlank()) {
            binding.etConceptoTres.setText(intent.getStringExtra("Concepto 3"))
        }
        if(!intent.getStringExtra("Concepto 4").isNullOrBlank()) {
            binding.etConceptoCuatro.setText(intent.getStringExtra("Concepto 4"))
        }
        if(!intent.getStringExtra("Concepto 5").isNullOrBlank()) {
            binding.etConceptoCinco.setText(intent.getStringExtra("Concepto 5"))
        }
    }
}