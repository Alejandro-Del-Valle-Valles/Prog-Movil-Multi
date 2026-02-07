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

    //Limits of the screen
    private var screenWidth = 0
    private var screenHeight = 0

    init {
        //Personalized font for the texts
        val customFont = ResourcesCompat.getFont(context, R.font.game_font)
        paint.typeface = customFont ?: Typeface.DEFAULT
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w
        screenHeight = h
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread?.start()
    }

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
            DifficultyTypes.DIFICIL -> squareSize = 100f
            DifficultyTypes.IMPOSIBLE -> squareSize = 50f
            else -> {/*Nada*/}
        }
        while (playing) {
            update()
            draw()
            control()
        }
    }

    /**
     * Update the speed of the squre if the selected difficluty is "Imposible"
     * and recalculate the remaining time.
     */
    private fun update() {
        if(difficulty == DifficultyTypes.IMPOSIBLE) {
            squareX += speedX
            squareY += speedY

            if(squareX > width || squareX < 0)
                speedX = -speedX

            if (squareY > height || squareY < 0)
                speedY = -speedY
        }
        //TODO: Añadir el cálculo de tiempo y puntaje
    }

    /**
     * Darw all the assets of the game on
     */
    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)
            paint.color = Color.WHITE
            paint.textSize = 60f
            canvas.drawText("Vidas: $numLives", 50f, 100f, paint)
            paint.color = squareColor //TODO: Cambiar esto a cuando se genere un nuevo cuadrado

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
     * Gestiona los toques en la pantalla
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //TODO: Añadir suma de puntaje en base al tiempo que ha tardado en pulsar sobre el cuadrado
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            if (touchX >= squareX && touchX <= (squareX + squareSize) &&
                touchY >= squareY && touchY <= (squareY + squareSize)) {
                val player = MediaPlayer.create(context, R.raw.tap_effect)
                player.start()
                player.setOnCompletionListener { player.release() }
                respawnSquare()
            } else {
                numLives--
                if (numLives <= 0) {
                    gameOver()
                }
            }
        }
        return true
    }

    /**
     * When the square is pressed, calculate new random cords for a new sqaure and change the color
     */
    private fun respawnSquare() {
        if (screenWidth > 0 && screenHeight > 0) {
            squareX = Random.nextFloat() * (screenWidth - squareSize)
            squareY = Random.nextFloat() * (screenHeight - squareSize)
            squareColor = squareColors.random()
        }
    }

    /**
     * When the player looses al lives, stops the game and start the LoseActivity
     */
    private fun gameOver() {
        playing = false
        val intent = Intent(context, LoseActivity::class.java)
        //Esto evita que se pueda volver al juego con la flecha de "atrás"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        if (context is Activity) {
            (context as Activity).finish()
        }
    }

}