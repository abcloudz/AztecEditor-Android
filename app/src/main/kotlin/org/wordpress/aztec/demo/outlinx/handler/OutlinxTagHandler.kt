package org.wordpress.aztec.demo.outlinx.handler

import android.app.Activity
import android.text.Editable
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import org.wordpress.aztec.Constants
import org.wordpress.aztec.demo.outlinx.AttachmentType
import org.wordpress.aztec.demo.outlinx.span.CenterMediaSpan
import org.wordpress.aztec.demo.outlinx.span.MediaClickableSpan
import org.wordpress.aztec.demo.outlinx.span.OutlinxMediaSpan
import org.wordpress.aztec.demo.outlinx.span.SpanMetadata
import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag
import org.wordpress.aztec.plugins.html2visual.IHtmlTagHandler
import org.wordpress.aztec.spans.HiddenHtmlSpan
import org.wordpress.aztec.spans.IAztecAttributedSpan
import org.wordpress.aztec.spans.IAztecNestable
import org.wordpress.aztec.util.getLast
import org.xml.sax.Attributes

abstract class OutlinxTagHandler(activity: Activity) : IHtmlTagHandler {

    protected val displayWidth: Int

    private val tagStack = mutableListOf<Any>()

    abstract val acceptableAttribute: AttachmentType

    abstract val clickCallback: ((SpanMetadata) -> Unit)

    abstract val spanKind: Class<*>

    init {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        displayWidth = metrics.widthPixels
    }

    override fun canHandleTag(tag: String): Boolean = AttachmentType.fromOutlinxTag(tag) == acceptableAttribute

    abstract fun createSpan(metadata: SpanMetadata, nestingLevel: Int): OutlinxMediaSpan

    override fun handleTag(opening: Boolean, tag: String, output: Editable, attributes: Attributes, nestingLevel: Int): Boolean {

        Log.d("outlinx", "nestingLevel: $nestingLevel")
        if (opening) {
            val metadata = SpanMetadata(
                    attributeType = acceptableAttribute,
                    attributeId = attributes.getValue(OutlinxTag.ATTRIBUTE_ID),
                    imageId = attributes.getValue(OutlinxTag.ATTRIBUTE_IMAGE_ID),
                    aspectRatio = attributes.getValue(OutlinxTag.ATTRIBUTE_RATIO).toFloat()
            )

            val mediaSpan = createSpan(metadata, nestingLevel)

            start(output, mediaSpan)
            start(output, MediaClickableSpan(mediaSpan, clickCallback))

//            val preformatStyle = BlockFormatter.PreformatStyle(0, 0.5f, 0, 0)
//            val alignSpan = AztecPreformatSpanAligned(nestingLevel, AztecAttributes(), preformatStyle, Layout.Alignment.ALIGN_CENTER)
//            start(output, alignSpan)
//            start(output, CenterMediaSpan(nestingLevel))
//            start(output, AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
            start(output, CenterMediaSpan(nestingLevel))
            output.append(Constants.IMG_CHAR)
        } else {
            end(output, CenterMediaSpan::class.java)
//            end(output, AztecPreformatSpanAligned::class.java)
//            end(output, CenterMediaSpan::class.java)
//            end(output, AlignmentSpan.Standard::class.java)
            end(output, MediaClickableSpan::class.java)
            end(output, spanKind)
        }

        return true
    }

    private fun start(output: Editable, mark: Any) {
        tagStack.add(mark)
        output.setSpan(mark, output.length, output.length, Spanned.SPAN_MARK_MARK)
    }

    private fun end(output: Editable, kind: Class<*>) {
        val last = if (tagStack.size > 0 && kind == tagStack[tagStack.size - 1].javaClass) {
            tagStack.removeAt(tagStack.size - 1) // remove and return the top mark on the stack
        } else {
            output.getLast(kind)
        }

        val start = output.getSpanStart(last)
        val end = output.length

        if (start != end) {
            output.setSpan(last, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (last is IAztecAttributedSpan) {
                // Apply the 'style' attribute if present
                last.applyInlineStyleAttributes(output, start, end)
            }
        } else if (start == end && IAztecNestable::class.java.isAssignableFrom(kind)) {
            // if block element is empty add a ZWJ to make it non empty and extend span
            if (HiddenHtmlSpan::class.java.isAssignableFrom(kind)) {
                output.append(Constants.MAGIC_CHAR)
            } else {
                output.append(Constants.ZWJ_CHAR)
            }
            output.setSpan(last, start, output.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

}
