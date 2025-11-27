package com.alejandro.tresenraya.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.tresenraya.R
import com.alejandro.tresenraya.data.StaticData
import com.alejandro.tresenraya.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var playerOne: String //Name of player One
    private lateinit var playerTwo: String //Name of player Two
    private var gameResult: Boolean? = null // null = game continues, true = there is a winner, false = tie
    private var dashboard: Array<Array<Boolean?>> = Array(3) { arrayOfNulls<Boolean>(3) }
    private var counter = 0 // Count the number of plays. Max 9 plays. Odd = player 1, Even = player 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerOne = intent.getStringExtra("PlayerOne").toString()
        playerTwo = intent.getStringExtra("PlayerTwo").toString()
        binding.tvPlayer.text = "Turno de $playerOne"
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

        if (counter % 2 != 0) { // Player 1 (odd)
            btImage.setImageResource(R.mipmap.cross)
            binding.tvPlayer.text = "Turno de $playerTwo"
            getPosition(btImage.tag.toString().toInt(), true) //True player 1
            gameResult = checkWinner()
            if (gameResult == true) showWinner(playerOne)
        } else { // Player 2 (even)
            btImage.setImageResource(R.mipmap.circle)
            binding.tvPlayer.text = "Turno de $playerOne"
            getPosition(btImage.tag.toString().toInt(), false) //False player 2
            gameResult = checkWinner()
            if (gameResult == true) showWinner(playerTwo)
        }

        // Check if there is a tie
        if (gameResult == false && counter == 9) {
            showWinner(StaticData.GAME_TIE)
        }
    }

    /**
     * Get the position of the button clicked and assign it to the dashboard array
     * @param position Int position of the button clicked
     * @param player Boolean true if player 1, false if player 2
     */
    private fun getPosition(position: Int, player: Boolean) {
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
     * Check if there is a winner.
     * @return Boolean: true if a player has won, false if there is a tie, null if the game continues.
     */
    private fun checkWinner(): Boolean? {
        // Check rows and columns
        for (i in 0..2) {
            if (dashboard[i][0] != null && dashboard[i][0] == dashboard[i][1]
                && dashboard[i][1] == dashboard[i][2]
            ) {
                return true // Winner in a row
            }
            if (dashboard[0][i] != null && dashboard[0][i] == dashboard[1][i]
                && dashboard[1][i] == dashboard[2][i]
            ) {
                return true // Winner in a column
            }
        }
        // Check diagonals
        if (dashboard[0][0] != null && dashboard[0][0] == dashboard[1][1]
            && dashboard[1][1] == dashboard[2][2]
        ) {
            return true // Winner in the main diagonal
        }
        if (dashboard[0][2] != null && dashboard[0][2] == dashboard[1][1]
            && dashboard[1][1] == dashboard[2][0]
        ) {
            return true // Winner in the secondary diagonal
        }

        // If all cells are filled and there is no winner, it's a tie
        if (counter == 9) {
            return false // Tie
        }

        // If no winner and the game is not over, return null
        return null
    }

    /**
     * Show the winner activity when the game is over
     * @param winner String the name of the winner
     */
    private fun showWinner(winner: String) {
        val intent = Intent(this, WinnerActivity::class.java)
        intent.putExtra("Winner", winner)
        startActivity(intent)
        finish()
    }
}