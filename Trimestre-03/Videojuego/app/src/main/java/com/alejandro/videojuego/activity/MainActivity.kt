package com.alejandro.videojuego.activity

import android.R
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.videojuego.databinding.ActivityMainBinding
import com.alejandro.videojuego.enums.DifficultyTypes

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<String>
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            DifficultyTypes.entries.map { it.name }
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spDificulty.adapter = adapter
        playMusic()

        binding.btStart.setOnClickListener {
            player?.stop()
            //TODO: Lanzar actividad del juego
        }
    }

    /**
     * Plays the music in a loop
     */
    private fun playMusic() {
        if(player == null) player= MediaPlayer.create(this, com.alejandro.videojuego.R.raw.menu_music)
        player?.start()
        player?.setOnCompletionListener { playMusic() }
    }
}