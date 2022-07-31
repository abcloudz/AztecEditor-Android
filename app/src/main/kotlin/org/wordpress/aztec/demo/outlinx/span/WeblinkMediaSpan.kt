package org.wordpress.aztec.demo.outlinx.span

import android.content.Context
import android.widget.TextView

class WeblinkMediaSpan(
        context: Context,
        resourceId: Int,
        textView: TextView,
        override var nestingLevel: Int,
        metadata: SpanMetadata,
        displayWidth: Int
) : OutlinxMediaSpan(context, resourceId, textView, nestingLevel, metadata, displayWidth) {

}
