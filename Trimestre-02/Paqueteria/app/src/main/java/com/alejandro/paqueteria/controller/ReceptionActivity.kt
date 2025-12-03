package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityReceptionBinding

class ReceptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}