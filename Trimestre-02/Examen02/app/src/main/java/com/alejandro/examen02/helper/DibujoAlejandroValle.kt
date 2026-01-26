package com.alejandro.examen02.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DibujoAlejandroValle (context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint: Paint = Paint()
    init {
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(300f, 300f, 200f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            paint.color = Color.YELLOW
            invalidate()
        }
        return true
    }
}
