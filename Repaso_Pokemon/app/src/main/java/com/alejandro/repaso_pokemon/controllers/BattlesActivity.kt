package com.alejandro.repaso_pokemon.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.repaso_pokemon.R
import com.alejandro.repaso_pokemon.databinding.ActivityBattlesBinding

class BattlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBattlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBattlesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}