package org.wordpress.aztec.demo.outlinx.span

import android.text.style.ClickableSpan
import android.view.View

class MediaClickableSpan(private val mediaSpan: OutlinxMediaSpan, private val callback: ((SpanMetadata) -> Unit)) : ClickableSpan() {

    override fun onClick(widget: View) {
        callback.invoke(mediaSpan.metadata)
    }
}