package com.alejandro.repaso_pokemon.controllers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.repaso_pokemon.databinding.ActivityBattlesBinding
import com.alejandro.repaso_pokemon.databinding.ActivityMainBinding
import com.alejandro.repaso_pokemon.databinding.ActivityManagerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btManage.setOnClickListener {
            val intent = Intent(this, ActivityManagerBinding::class.java)
            startActivity(intent)
        }

        binding.btBattle.setOnClickListener {
            val intent = Intent(this, ActivityBattlesBinding::class.java)
            startActivity(intent)
        }
    }
}