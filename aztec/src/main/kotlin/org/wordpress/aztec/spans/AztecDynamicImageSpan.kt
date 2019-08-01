package org.wordpress.aztec.spans

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.style.DynamicDrawableSpan
import android.util.Log
import org.wordpress.aztec.AztecText

private const val TAG = "AztecDynamicImageSpan"

abstract class AztecDynamicImageSpan(
    val context: Context,
    protected var imageDrawable: Drawable?,
    fixedWidthRes: Int = 0,
    fixedHeightRes: Int = 0
) : DynamicDrawableSpan() {
    var textView: AztecText? = null
    var aspectRatio: Double = 1.0
    private val fixedWidth = if (fixedWidthRes == 0) 0 else context.resources.getDimensionPixelSize(fixedWidthRes)
    private val fixedHeight = if (fixedHeightRes == 0) 0 else context.resources.getDimensionPixelSize(fixedHeightRes)

    private var measuring = false

    companion object {
        @JvmStatic
        protected fun setInitBounds(drawable: Drawable?) {
            drawable?.let {
                Log.d(TAG, "setInitBounds: w: ${it.intrinsicWidth} h: ${it.intrinsicHeight}")
                if (it.bounds.isEmpty && (it.intrinsicWidth > -1 || it.intrinsicHeight > -1)) {
                    it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                }
            }
        }

        @JvmStatic
        protected fun getWidth(drawable: Drawable?): Int {
            drawable?.let {
                if (it.intrinsicWidth < 0) {
                    // client may have set the bounds manually so, use those
                    Log.d(TAG, "getWidth bounds ${it.bounds.width()}")
                    return it.bounds.width()
                } else {
                    Log.d(TAG, "getWidth intrinsic ${it.intrinsicWidth}")
                    return it.intrinsicWidth
                }
            }

            return 0
        }

        @JvmStatic
        protected fun getHeight(drawable: Drawable?): Int {
            drawable?.let {
                if (it.intrinsicHeight < 0) {
                    // client may have set the bounds manually so, use those
                    Log.d(TAG, "getHeight bounds ${it.bounds.width()}")
                    return it.bounds.height()
                } else {
                    Log.d(TAG, "getHeight intrinsic ${it.intrinsicWidth}")
                    return it.intrinsicHeight
                }
            }

            return 0
        }
    }

    init {
        computeAspectRatio()

        setInitBounds(imageDrawable)
    }

    fun computeAspectRatio() {
        if ((imageDrawable?.intrinsicWidth ?: -1) > -1 && (imageDrawable?.intrinsicHeight ?: -1) > -1) {
            aspectRatio = 1.0 * (imageDrawable?.intrinsicWidth ?: 1) / (imageDrawable?.intrinsicHeight ?: 1)
        } else if (!(imageDrawable?.bounds?.isEmpty ?: true)) {
            aspectRatio = 1.0 * (imageDrawable?.bounds?.width() ?: 0) / (imageDrawable?.bounds?.height() ?: 1)
        } else {
            aspectRatio = 1.0
        }
    }

    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, metrics: Paint.FontMetricsInt?): Int {
        val sizeRect = adjustBounds(start)
        Log.i(TAG, " getSize $sizeRect")

        if (metrics != null && sizeRect.width() > 0) {
            metrics.ascent = -sizeRect.height()
            metrics.descent = 0

            metrics.top = metrics.ascent
            metrics.bottom = 0
        }

        return if (textView == null) {
            sizeRect.width()
        } else {
            (textView!!.width - (textView!!.width - sizeRect.width()) / 2) + 100
        }
    }

    fun adjustBounds(start: Int): Rect {
        if (fixedWidth > 0 && fixedHeight > 0) {
            return Rect(0, 0, fixedWidth, fixedHeight)
        }
        Log.w(TAG, "====================================")
        if (textView == null || textView?.widthMeasureSpec == 0) {
            Log.w(TAG, "TextView not ready")
            return Rect(imageDrawable?.bounds ?: Rect(0, 0, 0, 0))
        }

        val layout = textView?.layout

        if (measuring || layout == null) {
            // if we're in pre-layout phase, just return a tiny rect
            Log.w(TAG, "layout not ready or measuring")
            return Rect(0, 0, 1, 1)
        }

        val line = layout.getLineForOffset(start)
        Log.w(TAG, "line: $line")

        val maxWidth = layout.getParagraphRight(line) - layout.getParagraphLeft(line)
        Log.w(TAG, "maxWidth: $maxWidth")

        // use the original bounds if non-zero, otherwise try the intrinsic sizes. If those are not available then
        //  just assume maximum size.

        var width = if ((imageDrawable?.intrinsicWidth ?: -1) > -1) imageDrawable?.intrinsicWidth ?: -1
        else maxWidth
        Log.w(TAG, "width: $width")
        var height = if ((imageDrawable?.intrinsicHeight ?: -1) > -1) imageDrawable?.intrinsicHeight ?: -1
        else (width / aspectRatio).toInt()
        Log.w(TAG, "height: $height")

        if (width > maxWidth) {
            width = maxWidth
            height = (width / aspectRatio).toInt()
            Log.w(TAG, "width > maxWidth: $width x $height")
        }

        imageDrawable?.bounds = Rect(0, 0, width, height)
        Log.w(TAG, "imageDrawable exists ${imageDrawable != null}")

        return Rect(imageDrawable?.bounds ?: Rect(0, 0, 0, 0))
    }

    override fun getDrawable(): Drawable? {
        return imageDrawable
    }

    open fun setDrawable(newDrawable: Drawable?) {
        imageDrawable = newDrawable

        setInitBounds(newDrawable)

        computeAspectRatio()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        canvas.save()

        if (imageDrawable != null) {
            var transY = top
            if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE) {
                transY -= paint.fontMetricsInt.descent
            }

            canvas.translate(x, transY.toFloat())
            imageDrawable!!.draw(canvas)
        }

        canvas.restore()
    }
}
