package org.wordpress.aztec.spans

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Layout
import org.wordpress.aztec.AztecAttributes
import org.wordpress.aztec.AztecText

class AztecImageSpan(
    context: Context, drawable: Drawable?,
    override var nestingLevel: Int,
    attributes: AztecAttributes = AztecAttributes(),
    fixedWidthRes: Int,
    fixedHeightRes: Int,
    var onImageTappedListener: AztecText.OnImageTappedListener? = null,
    onMediaDeletedListener: AztecText.OnMediaDeletedListener? = null,
    editor: AztecText? = null
) : IAztecCenteredImageSpan,
    AztecMediaSpan(context, drawable, attributes, onMediaDeletedListener, editor, fixedWidthRes, fixedHeightRes) {
    override val TAG: String = "img"

    override fun onClick() {
        onImageTappedListener?.onImageTapped(attributes, getWidth(imageDrawable), getHeight(imageDrawable))
    }
}
