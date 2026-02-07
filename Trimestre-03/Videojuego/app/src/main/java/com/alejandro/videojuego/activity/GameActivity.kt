package com.alejandro.videojuego.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.videojuego.enums.DifficultyTypes
import com.alejandro.videojuego.game.GameView

class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dificulty = DifficultyTypes.valueOf(intent.getStringExtra("DIFFICULTY")
            ?: DifficultyTypes.FACIL.name)
        gameView = GameView(this, dificulty)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
}