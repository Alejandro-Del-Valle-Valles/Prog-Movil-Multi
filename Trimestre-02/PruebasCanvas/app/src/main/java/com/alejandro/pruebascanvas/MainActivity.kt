package com.alejandro.pruebascanvas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.pruebascanvas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCirculo.setOnClickListener {
            intent = Intent(this, DibujoActivity::class.java)
            intent.putExtra("forma", "Circulo")
            startActivity(intent)
        }

        binding.btRectangulo.setOnClickListener {
            intent = Intent(this, DibujoActivity::class.java)
            intent.putExtra("forma", "Rectangulo")
            startActivity(intent)
        }

        binding.btLinea.setOnClickListener {
            intent = Intent(this, DibujoActivity::class.java)
            intent.putExtra("forma", "Linea")
            startActivity(intent)
        }
    }
}