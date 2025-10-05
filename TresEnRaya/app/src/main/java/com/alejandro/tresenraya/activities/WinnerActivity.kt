package com.alejandro.tresenraya.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.tresenraya.data.StaticData
import com.alejandro.tresenraya.databinding.ActivityWinnerBinding

class WinnerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinnerBinding
    private lateinit var winner: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        winner = intent.getStringExtra("Winner").toString()
        if(winner == StaticData.GAME_TIE) binding.tvWinner.text = "¡Empate!"
        else binding.tvWinner.text = "¡Ha ganado $winner!"
    }

    /**
     * When the exit button is clicked, finish the activity and return to the main
     * @param view View the view that was clicked
     */
    fun onExitClicked(view: View) {
        finish()
    }
}