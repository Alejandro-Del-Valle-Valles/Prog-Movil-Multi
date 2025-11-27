package com.alejandro.registrosimple.controladores

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.registrosimple.R
import com.alejandro.registrosimple.databinding.ActivitySesionBinding

class SesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSalirInicio.setOnClickListener {
            finish()
        }
    }
}