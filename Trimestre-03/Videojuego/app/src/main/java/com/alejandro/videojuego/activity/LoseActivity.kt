package com.alejandro.videojuego.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.videojuego.R
import com.alejandro.videojuego.databinding.ActivityLoseBinding

class LoseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btExit.setOnClickListener {
            finish()
            //Vuelve al menú de inicio y reanuda la música.
        }
    }
}