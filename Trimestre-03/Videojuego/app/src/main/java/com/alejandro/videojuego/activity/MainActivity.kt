package com.alejandro.videojuego.activity

import android.R
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.videojuego.databinding.ActivityMainBinding
import com.alejandro.videojuego.enums.DifficultyTypes
import com.alejandro.videojuego.R.raw //Para que no falle

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
            intent = Intent(this, GameActivity::class.java)
            intent.putExtra("DIFFICULTY", binding.spDificulty.selectedItem.toString())
            startActivity(intent)
        }
    }

    /**
     * Plays the music in a loop
     */
    private fun playMusic() {
        if(player == null) player = MediaPlayer.create(this, raw.menu_music)
        player?.start()
        player?.setOnCompletionListener { playMusic() }
    }

    override fun onResume() {
        super.onResume()
        playMusic()
    }

    override fun onPause() {
        super.onPause()
        player?.stop()
        player?.release()
    }
}