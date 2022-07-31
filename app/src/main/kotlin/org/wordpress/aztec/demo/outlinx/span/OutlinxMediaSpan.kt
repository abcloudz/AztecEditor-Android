package org.wordpress.aztec.demo.outlinx.span

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.DynamicDrawableSpan
import android.util.Log
import android.widget.TextView
import org.wordpress.aztec.demo.R
import org.wordpress.aztec.spans.IAztecFullWidthImageSpan

abstract class OutlinxMediaSpan(val context: Context, private val resourceId: Int, private val textView: TextView, override var nestingLevel: Int, val metadata: SpanMetadata, val displayWidth: Int) : DynamicDrawableSpan(ALIGN_BASELINE), IAztecFullWidthImageSpan {

    private val spanWidth: Int
        get() = (displayWidth - (displayWidth * .1f)).toInt()

    private val spanHeight: Int
        get() = (spanWidth * 3f / 4).toInt()

    override fun updateMeasureState(p: TextPaint) {
        super.updateMeasureState(p)
        Log.d("outlinx", "width1: ${textView.width}")
    }

    private val _drawable = context.getDrawable(resourceId)?.apply {
        setBounds(0, 0, spanWidth, spanHeight)
    }

    fun setDrawable() {

    }

    override fun getDrawable(): Drawable? {
        val drawable = context.getDrawable(resourceId)

        Log.d("outlinx", "width: ${textView.width}")
        drawable?.setBounds(0, 0, spanWidth, spanHeight)
        return drawable
    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint)
//        left, top, right, bottom, paint
        canvas.drawRect(x, top.toFloat(), y.toFloat(), bottom.toFloat(), Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        })

//        val owrl = context.getDrawable(R.drawable.aztec)
//        owrl?.setBounds(100, 0, spanWidth + 100, spanHeight)
//        owrl?.draw(canvas)

        Log.i("outlinx", "draw: start $start, end: $end, x: $x, top: $top, y: $y, bottom: $bottom")
    }
}