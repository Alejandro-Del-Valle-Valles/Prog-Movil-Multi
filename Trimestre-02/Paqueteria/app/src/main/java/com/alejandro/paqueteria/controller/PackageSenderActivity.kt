package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityPackageSenderBinding

class PackageSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackageSenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPackageSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}