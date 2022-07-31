package org.wordpress.aztec.demo.outlinx.handler

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import org.wordpress.aztec.demo.R
import org.wordpress.aztec.demo.outlinx.AttachmentType
import org.wordpress.aztec.demo.outlinx.span.OutlinxMediaSpan
import org.wordpress.aztec.demo.outlinx.span.SpanMetadata
import org.wordpress.aztec.demo.outlinx.span.WeblinkMediaSpan

class WeblinkTagHandler(private val context: Context, private val textView: TextView, activity: Activity) : OutlinxTagHandler(activity) {

    override val acceptableAttribute: AttachmentType
        get() = AttachmentType.WEBLINK

    override val clickCallback: (SpanMetadata) -> Unit
        get() = {
            Log.d("outlinx", "Weblink temp click")
        }

    override val spanKind: Class<*>
        get() = WeblinkMediaSpan::class.java

    override fun createSpan(metadata: SpanMetadata, nestingLevel: Int): OutlinxMediaSpan {
        return WeblinkMediaSpan(context, R.drawable.ic_note_placeholder, textView, nestingLevel, metadata, displayWidth)
    }
}