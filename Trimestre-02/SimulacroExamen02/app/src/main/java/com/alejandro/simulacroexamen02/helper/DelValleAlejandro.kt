package com.alejandro.simulacroexamen02.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DelValleAlejandro(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint: Paint = Paint()

    var tipo: String = "fuego"
        set(value) {
            field = value.lowercase()
            invalidate()
        }

    var size: Int = 10
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centroX = width / 2f
        val centroY = height / 2f

        when(tipo) {
            "fuego" -> {
                paint.color = Color.RED
                canvas.drawCircle(centroX, centroY, size * 10f, paint)
            }
            "hielo" -> {
                paint.color = Color.BLUE
                canvas.drawRect(centroX - 250f, centroY - 150f, centroX +  250f, centroY  + 150f, paint)
            }
            "rayo" -> {
                paint.color = Color.YELLOW
                paint.strokeWidth = size * 10f
                canvas.drawLine(centroX, centroY, centroX + 400f, centroY + 100f, paint)
            }
        }
    }


}