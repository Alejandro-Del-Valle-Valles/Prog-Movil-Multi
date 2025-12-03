package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityLetterSenderBinding
import com.alejandro.paqueteria.databinding.ActivityMainBinding

class LetterSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterSenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLetterSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}