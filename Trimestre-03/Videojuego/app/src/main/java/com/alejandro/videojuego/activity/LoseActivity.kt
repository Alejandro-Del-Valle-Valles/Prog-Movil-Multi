package com.alejandro.videojuego.activity

import android.media.MediaPlayer
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

        val points = intent.getIntExtra("POINTS", 0)

        binding.tvPoints.setText("Total de puntos obtenidos: $points")

        val player = MediaPlayer.create(this, R.raw.lose_effect)
        player.start()
        player.setOnCompletionListener { player.release() }

        binding.btExit.setOnClickListener {
            finish()
        }
    }
}