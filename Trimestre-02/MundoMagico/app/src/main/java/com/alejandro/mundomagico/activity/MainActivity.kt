package com.alejandro.mundomagico.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.mundomagico.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btManageVaritas.setOnClickListener {
            intent = Intent(this, ListVaritaActivity::class.java)
            startActivity(intent)
        }

        binding.btCreateVarita.setOnClickListener {
            intent = Intent(this, VaritaActivity::class.java)
            startActivity(intent)
        }
    }
}