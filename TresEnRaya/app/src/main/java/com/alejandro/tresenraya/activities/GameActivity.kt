package com.alejandro.tresenraya.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.tresenraya.R
import com.alejandro.tresenraya.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var dashboard: Array<Array<Boolean?>> = Array(3) { arrayOfNulls<Boolean>(3) }
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvPlayer.text = "Turno de ${intent.getStringExtra("PlayerOne")}"
    }

    /**
     * Handle the click event of the cells of the dashboard
     * If the player 1 clicks, set the image to cross, else set the image to a circle.
     * Disable the button clicked and check if there is a winner.
     * @param view View the view that was clicked
     */
    fun onCellClicked(view: View) {
        val btImage = view as ImageButton
        btImage.isEnabled = false
        counter++
        if (counter % 2 == 0) {
            btImage.setImageResource(R.mipmap.cross)
            binding.tvPlayer.text = "Turno de ${intent.getStringExtra("PlayerTwo")}"
            getPosition(btImage.tag.toString().toInt(), true) //True player 1
            if(checkWinner()) finish() //TODO: Pasar al ganador y pasarle el nombre
        }
        else {
            btImage.setImageResource(R.mipmap.circle)
            binding.tvPlayer.text = "Turno de ${intent.getStringExtra("PlayerOne")}"
            getPosition(btImage.tag.toString().toInt(), false) //False player 2
            if(checkWinner()) finish() //TODO: Pasar al ganador y pasarle el nombre
        }
    }

    /**
     * Get the position of the button clicked and assign it to the dashboard array
     * @param position Int position of the button clicked
     * @param player Boolean true if player 1, false if player 2
     */
    fun getPosition(position: Int, player: Boolean) {
        when (position) {
            0 -> dashboard[0][0] = player
            1 -> dashboard[0][1] = player
            2 -> dashboard[0][2] = player
            3 -> dashboard[1][0] = player
            4 -> dashboard[1][1] = player
            5 -> dashboard[1][2] = player
            6 -> dashboard[2][0] = player
            7 -> dashboard[2][1] = player
            8 -> dashboard[2][2] = player
        }
    }

    /**
     * Check if there is a winner
     * @return Boolean true if there is a winner, false otherwise
     */
    fun checkWinner(): Boolean {
        // Check rows and columns
        for (i in 0..2) {
            if (dashboard[i][0] != null && dashboard[i][0] == dashboard[i][1]
                && dashboard[i][1] == dashboard[i][2]) {
                return true
            }
            if (dashboard[0][i] != null && dashboard[0][i] == dashboard[1][i]
                && dashboard[1][i] == dashboard[2][i]) {
                return true
            }
        }
        // Check diagonals
        if (dashboard[0][0] != null && dashboard[0][0] == dashboard[1][1]
            && dashboard[1][1] == dashboard[2][2]) {
            return true
        }
        if (dashboard[0][2] != null && dashboard[0][2] == dashboard[1][1]
            && dashboard[1][1] == dashboard[2][0]) {
            return true
        }
        return false
    }
}