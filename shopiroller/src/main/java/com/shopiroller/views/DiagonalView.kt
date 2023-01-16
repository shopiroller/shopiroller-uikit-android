package com.shopiroller.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.shopiroller.R

class DiagonalView : View {
    private var dividerColor = 0
    private var paint: Paint? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private fun init(context: Context) {
        dividerColor = ContextCompat.getColor(context, R.color.divider_color)
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.color = dividerColor
        paint!!.strokeWidth = 3F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint!!)
    }
}