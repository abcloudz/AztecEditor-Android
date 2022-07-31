package org.wordpress.aztec.demo.outlinx

import android.app.Activity
import android.util.Log
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.Constants
import org.wordpress.aztec.EnhancedMovementMethod
import org.wordpress.aztec.demo.outlinx.handler.WeblinkTagHandler
import org.wordpress.aztec.demo.outlinx.span.OutlinxMediaSpan
import org.wordpress.aztec.demo.outlinx.span.WeblinkMediaSpan
import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag

fun Aztec.addOutlinxPlugins(activity: Activity): Aztec {
    addPlugin(OutlinxMediaPreprocessor())
    addPlugin(WeblinkTagHandler(activity, visualEditor, activity))
    addPlugin(MediaInlineSpanHandler())
    setMediaCallback {
        loadImages()
    }
    visualEditor.movementMethod = OutlinxMovementMethod
    OutlinxMovementMethod.isLinkTapEnabled = true
//    OutlinxMovementMethod.linkTappedListener
    OutlinxMovementMethod.taskListClickHandler = EnhancedMovementMethod.taskListClickHandler

    this.visualEditor.toPlainHtml()

    return this
}

fun Aztec.loadImages() {
    val outlinxSpans = visualEditor.text.getSpans(0, visualEditor.text.length, OutlinxMediaSpan::class.java)
    Log.d("outlinx", "spans: ${outlinxSpans.size}")
    outlinxSpans.filterIsInstance<WeblinkMediaSpan>()
            .onEach {
                it.drawable
            }
}

fun Aztec.getHtml(): String = visualEditor.toHtml()
        .replace(Constants.IMG_STRING, "")
        .replace("<${OutlinxTag.TAG_MEDIA_CENTER}>", "")
        .replace("</${OutlinxTag.TAG_MEDIA_CENTER}>", "")