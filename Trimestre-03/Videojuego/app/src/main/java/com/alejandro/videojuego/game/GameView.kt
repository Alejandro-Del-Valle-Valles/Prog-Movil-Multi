package com.alejandro.videojuego.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat
import com.alejandro.videojuego.R
import com.alejandro.videojuego.activity.LoseActivity
import com.alejandro.videojuego.enums.DifficultyTypes
import kotlinx.coroutines.Runnable
import kotlin.random.Random

class GameView(context: Context, val difficulty: DifficultyTypes): SurfaceView(context), Runnable {
    private var gameThread: Thread? = null
    private var numLives = 3

    @Volatile
    private var playing = false

    //Graphic objects
    private val surfaceHolder = holder
    private val paint = Paint()

    //Game Assets
    private val squareColors = listOf<Int>(Color.CYAN, Color.RED, Color.GREEN, Color.MAGENTA)
    private var squareColor = squareColors.random()
    private var squareX = 100f
    private var squareY = 100f
    private var squareSize: Float = 250f //Default (Modo Facil) 300
    private var speedX = 15f
    private var speedY = 15f
    private var points = 0

    //Max time to touch the square
    private var maxTime = 5f //First try of each difficulty will have 5 seconds
    private var timeRemaining = maxTime

    //Limits of the screen
    private var screenWidth = 0
    private var screenHeight = 0
    private val topUIHeight = 75f //Space top reserved for information (Lives, time, points)

    init {
        //Personalized font for the texts
        val customFont = ResourcesCompat.getFont(context, R.font.game_font)
        paint.typeface = customFont ?: Typeface.DEFAULT
        paint.textSize = 60f
    }

    /**
     * Set the limits of the screen if the screen size is changed
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w
        screenHeight = h
    }

    /**
     * Resume the game when it's on the screen
     */
    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread?.start()
    }

    /**
     * Pause the game when it isn't on the screen
     */
    fun pause() {
        playing = false
        try {
            gameThread?.join()
        } catch (ex: InterruptedException) {

        }
    }

    /**
     * Change the square size based on the selected difficulty
     * and execute all the functions to run the game.
     */
    override fun run() {
        when(difficulty) {
            DifficultyTypes.NORMAL -> squareSize = 175f
            DifficultyTypes.DIFICIL -> {
                squareSize = 100f
                maxTime = 3f
            }
            DifficultyTypes.IMPOSIBLE -> {
                squareSize = 50f
                maxTime = 3f
            }
            else -> {}
        }
        var lastFrameTime = System.currentTimeMillis()

        while (playing) {
            //Calculate the time of the actual frame
            val currentTime = System.currentTimeMillis()
            val newTime = (currentTime - lastFrameTime) / 1000f
            lastFrameTime = currentTime

            update(newTime)
            draw()
            control()
        }
    }

    /**
     * Update the speed of the squre if the selected difficluty is "Imposible"
     * and recalculate the remaining time.
     */
    private fun update(newTime: Float) {
        timeRemaining -= newTime
        if(difficulty == DifficultyTypes.IMPOSIBLE) {
            squareX += speedX
            squareY += speedY

            if(squareX + squareSize > width || squareX < 0)
                speedX = -speedX

            if (squareY + squareSize > height || squareY < topUIHeight)
                speedY = -speedY
        }
        if(timeRemaining <= 0f) {
            respawnSquare()
            numLives--
            timeRemaining = maxTime
        }
        if(numLives <= 0) gameOver()

    }

    /**
     * Draw all the assets of the game on
     */
    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)
            paint.color = Color.WHITE
            canvas.drawText("Vidas: $numLives", 25f, 100f, paint)
            canvas.drawText("Tiempo: ${"%.2f".format(timeRemaining)}", 275f, 100f, paint)
            canvas.drawText("Puntos: $points", 650f, 100f, paint)
            paint.color = squareColor

            canvas.drawRect(squareX, squareY, squareX + squareSize, squareY + squareSize, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    /**
     * Manage the FPS of the game (60 FPS)
     */
    private fun control() {
        try {
            Thread.sleep(17)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }

    /**
     * Check when the player touch the square. If the player touch the square, play a sound effect
     * adds points and call the function tha generates a new square.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            if (touchX >= squareX && touchX <= (squareX + squareSize) &&
                touchY >= squareY && touchY <= (squareY + squareSize)) {
                val player = MediaPlayer.create(context, R.raw.tap_effect)
                player.start()
                player.setOnCompletionListener { player.release() }
                respawnSquare()
                calculatePoints()
            } else {
                numLives--
                if (numLives <= 0) {
                    gameOver()
                }
            }
            timeRemaining = maxTime
        }
        return true
    }

    /**
     * Calculate the points to give in base of the time remaining
     */
    private fun calculatePoints() {
        points += when {
            timeRemaining >= 4f -> 1000
            timeRemaining >= 3f -> 500
            timeRemaining >= 2f -> 250
            timeRemaining >= 1f -> 100
            else -> 50
        }
    }

    /**
     * When the square is pressed, calculate new random cords for a new sqaure and change the color
     */
    private fun respawnSquare() {
        if (screenWidth > 0 && screenHeight > 0) {
            squareX = Random.nextFloat() * (screenWidth - squareSize)
            val verticalRange = screenHeight - topUIHeight - squareSize
            squareY = (Random.nextFloat() * verticalRange) + topUIHeight
            squareColor = squareColors.random()
        }
    }

    /**
     * When the player looses al lives, stops the game and start the LoseActivity
     */
    private fun gameOver() {
        playing = false
        val intent = Intent(context, LoseActivity::class.java)
        intent.putExtra("POINTS", points)
        context.startActivity(intent)

        if (context is Activity) {
            (context as Activity).finish()
        }
    }

}