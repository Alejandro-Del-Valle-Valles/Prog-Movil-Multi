package com.alejandro.recetas.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.recetas.R
import com.alejandro.recetas.databinding.ActivityIngredientsBinding

class IngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}