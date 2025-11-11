package com.alejandro.ejerciciorepaso01

import android.content.Intent
import android.os.Bundle
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

        binding.btConceptos.setOnClickListener {
            intent = Intent(this, ConceptosActivity::class.java)
            conceptos.forEach { intent.putExtra(it, it) }
            startActivity(intent)
            //TODO: SI cancela que se guarden lso últimos datos, si no, que se actualicen.
            //Pasar textos vaciós donde se va a guardar los datos
        }
    }

    private fun checkConcpetosIsNotNull(): Boolean {
        var esCorrecto = true

        return false
    }
}