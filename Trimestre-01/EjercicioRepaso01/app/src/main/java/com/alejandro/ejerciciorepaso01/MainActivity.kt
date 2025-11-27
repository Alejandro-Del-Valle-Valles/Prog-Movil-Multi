package com.alejandro.ejerciciorepaso01

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.ejerciciorepaso01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val conceptos = arrayOfNulls<String>(5)
    private val cantidades = arrayOfNulls<Int>(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDatosIntent()
        binding.btConceptos.setOnClickListener {
            intent = Intent(this, ConceptosActivity::class.java)
            for(i in conceptos.indices) {
                intent.putExtra("Concepto " + (i + 1), conceptos[i])
            }
            startActivity(intent)
            finish()
        }

        binding.btCantidades.setOnClickListener {
            if(checkConceptosIsNotNull()) {
                intent = Intent(this, CantidadesActivity::class.java)
                for(i in conceptos.indices) {
                    intent.putExtra("Concepto " + (i + 1), conceptos[i])
                }
                for(i in cantidades.indices) {
                    intent.putExtra("Cantidad " + (i + 1), cantidades[i])
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,
                    "No has rellenado al menos lo 4 primeros conceptos.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.btResumen.setOnClickListener {
            if(checkConceptosIsNotNull() &&
                checkCantidadesIsNotNull()) {
                intent = Intent(this, ResumenActivity::class.java)
                for(i in conceptos.indices) {
                    intent.putExtra("Concepto " + (i + 1), conceptos[i])
                }
                for(i in cantidades.indices) {
                    intent.putExtra("Cantidad " + (i + 1), cantidades[i])
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,
                    "No has rellenado los 4 primeros conceptos o sus cantidades.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkConceptosIsNotNull(): Boolean {
        var esCorrecto = true
        for(i in 0..conceptos.size - 1) {
            if(conceptos[i] == null) {
                esCorrecto = false
                break
            }
        }
        return esCorrecto
    }

    private fun checkCantidadesIsNotNull(): Boolean {
        var esCorrecto = true
        for(i in 0..cantidades.size - 1) {
            if(cantidades[i] == null) {
                esCorrecto = false
                break
            }
        }
        return esCorrecto
    }

    private fun getDatosIntent() {
        for(i in conceptos.indices) {
            conceptos[i] = intent.getStringExtra("Concepto " + (i + 1))
        }

        for(i in cantidades.indices) {
            val llave = "Cantidad " + (i + 1)
            if(intent.hasExtra(llave)) {
                cantidades[i] = intent.getIntExtra(llave, -1)
            } else {
                cantidades[i] = null
            }
        }
    }
}