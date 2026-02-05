package com.alejandro.pruebascanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MiDibujo(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint: Paint = Paint()

    var forma: String = "Circulo"
        set(value) {
            field = value
            invalidate()
        }
    var nuevoColor: Int = Color.RED
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

        when(forma) {
            "Circulo" -> {
                paint.color = nuevoColor
                canvas.drawCircle(centroX, centroY, 200f, paint)
            }
            "Rectangulo" -> {
                paint.color = nuevoColor
                canvas.drawRect(centroX - 150f,
                    centroY - 100f,
                    centroX + 150f,
                    centroY + 100f,
                    paint)
            }
            "Linea" -> {
                paint.color = nuevoColor
                canvas.drawLine(centroX, centroY,
                    centroX + 400f, centroY + 70f, paint)
            }
            "Texto" -> {
                paint.textSize = 68f
                paint.isFakeBoldText = false
                paint.color = nuevoColor
                canvas.drawText("Este texto es de prueba",
                    centroX,
                    centroY,
                    paint)
            }
        }
    }
}