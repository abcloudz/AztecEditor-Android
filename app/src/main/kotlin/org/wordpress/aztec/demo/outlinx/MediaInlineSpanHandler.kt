package org.wordpress.aztec.demo.outlinx

import android.text.style.CharacterStyle
import android.util.Log
import org.wordpress.aztec.demo.outlinx.span.MediaClickableSpan
import org.wordpress.aztec.demo.outlinx.span.OutlinxMediaSpan
import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag
import org.wordpress.aztec.plugins.visual2html.IInlineSpanHandler

class MediaInlineSpanHandler : IInlineSpanHandler {

    override fun canHandleSpan(span: CharacterStyle): Boolean = span is OutlinxMediaSpan || span is MediaClickableSpan

    override fun handleSpanStart(html: StringBuilder, span: CharacterStyle) {
        if (span is OutlinxMediaSpan) {
            val metadata = span.metadata
            html.append(OutlinxTag.buildOriginTag(
                    attachmentType = metadata.attributeType.type,
                    attachmentId = metadata.attributeId,
                    imageId = metadata.imageId,
                    aspectRatio = metadata.aspectRatio
            ))
        }
    }

    override fun handleSpanEnd(html: StringBuilder, span: CharacterStyle) {
        Log.d("outlinx", "handleSpanEnd: ${span.javaClass.simpleName}")
    }
}