package com.alejandro.recetas.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.recetas.R
import com.alejandro.recetas.databinding.ActivityResumeBinding

class ResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResumeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}