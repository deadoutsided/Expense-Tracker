package com.example.expensetrracker.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    init {
        // Начальный цвет круга
        paint.color = 0xFFFF0000.toInt()//.toInt() // Красный
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = Math.min(width, height) / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    // Метод для изменения цвета круга
    fun setCircleColor(color: Int) {
        paint.color = color
        invalidate() // Перерисовка круга с новым цветом
    }

    fun getCitcleColor(): Int {
        return paint.color
    }
}
