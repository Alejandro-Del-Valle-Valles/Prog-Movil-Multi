package com.alejandro.practicatipoexamen.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.practicatipoexamen.R
import com.alejandro.practicatipoexamen.databinding.ActivityCartasBinding

class CartasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}