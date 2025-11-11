package com.alejandro.ejerciciorepaso01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.ejerciciorepaso01.databinding.ActivityConceptosBinding

class ConceptosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConceptosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConceptosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}