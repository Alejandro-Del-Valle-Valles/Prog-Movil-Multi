package com.alejandro.pruebascanvas

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.pruebascanvas.databinding.ActivityDibujoBinding

class DibujoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDibujoBinding
    private val colores = listOf<Int>(Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN)
    private val formas = listOf<String>("Circulo", "Rectangulo", "Linea", "Texto")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDibujoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var forma = intent.getStringExtra("forma")
        binding.cvDibujo.nuevoColor = colores.random()
        binding.cvDibujo.forma = forma ?: formas.random()

        binding.btVolver.setOnClickListener {
            finish()
        }

        binding.btRecargar.setOnClickListener {
            var nuevaForma: String
            do {
                nuevaForma = formas.random()
            }while (nuevaForma.equals(forma))
            forma = nuevaForma //Evito que se genere dos veces la misma forma
            binding.cvDibujo.nuevoColor = colores.random()
            binding.cvDibujo.forma = forma
        }
    }
}