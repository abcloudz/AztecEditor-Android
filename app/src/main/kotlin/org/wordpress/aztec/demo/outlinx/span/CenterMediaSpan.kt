package org.wordpress.aztec.demo.outlinx.span

import android.text.Layout
import android.text.style.AlignmentSpan
import org.wordpress.aztec.AztecAttributes
import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag
import org.wordpress.aztec.spans.IAztecParagraphStyle

class CenterMediaSpan(override var nestingLevel: Int) : AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), IAztecParagraphStyle {
    override var attributes: AztecAttributes = AztecAttributes()
    override val TAG: String
        get() = OutlinxTag.TAG_MEDIA_CENTER
}