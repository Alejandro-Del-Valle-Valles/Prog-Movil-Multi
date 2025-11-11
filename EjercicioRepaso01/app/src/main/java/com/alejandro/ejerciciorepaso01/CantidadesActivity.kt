package com.alejandro.ejerciciorepaso01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.ejerciciorepaso01.databinding.ActivityCantidadesBinding

class CantidadesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCantidadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCantidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}