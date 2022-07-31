package org.wordpress.aztec.demo.outlinx

import android.util.Log
import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag
import org.wordpress.aztec.plugins.html2visual.IHtmlPreprocessor

class OutlinxMediaPreprocessor : IHtmlPreprocessor {

    private val regex = """<img .+?(?=image://)image://.+?(?=>)>""".toRegex()

    private val imageRegex = """image://.+?(?=\?)""".toRegex()
    private val attachmentTypeRegex = """${OutlinxTag.ORIGIN_ATTR_ATTACHMENT_TYPE}=.+?(?=&)""".toRegex()
    private val attachmentIdRegex = """${OutlinxTag.ORIGIN_ATTR_ATTACHMENT_ID}=.+?(?=&)""".toRegex()
    private val aspectRatioRegex = """${OutlinxTag.ORIGIN_ATTR_ASPECT_RATIO}=.+?(?=")""".toRegex()

    override fun beforeHtmlProcessed(source: String): String {
        Log.d("outlinx", source)
        val output = source.replace(regex) { matchResult ->
            val result = matchResult.value
            if (result.isNewMediaTag()) {
                buildNewTag(result) ?: source
            } else {
                buildOldTag(result) ?: source
            }
        }
        Log.i("outlinx", output)
        return output
    }

    private fun String.isNewMediaTag() = contains(ATTR_ATTACHMENT_TYPE) && contains(ATTR_ATTACHMENT_ID) && contains(ATTR_ASPECT_RATIO)

    private fun buildOldTag(input: String): String? {
        val imageId = input.imageId() ?: return null

        return OutlinxTag.buildTag(
                attachmentId = imageId,
                attachmentType = AttachmentType.IMAGE.type,
                imageId = imageId,
                aspectRatio = input.aspectRatio()
        )
    }

    private fun buildNewTag(input: String): String? {
        val imageId = input.imageId() ?: return null
        val attachmentType = input.attachmentType() ?: return null
        val attachmentId = input.attachmentId() ?: return null

        return OutlinxTag.buildTag(
                attachmentId = attachmentId,
                attachmentType = attachmentType,
                imageId = imageId,
                aspectRatio = input.aspectRatio()
        )
    }

    private fun String.imageId(): String? {
        val imageFind = imageRegex.find(this)?.value ?: return null
        return imageFind.substring(imageFind.lastIndexOf("/") + 1)
    }

    private fun String.attachmentType(): String? {
        val find = attachmentTypeRegex.find(this)?.value ?: return null
        return find.substring(find.lastIndexOf("=") + 1)
    }

    private fun String.attachmentId(): String? {
        val find = attachmentIdRegex.find(this)?.value ?: return null
        return find.substring(find.lastIndexOf("=") + 1)
    }

    private fun String.aspectRatio(): Float {
        val find = aspectRatioRegex.find(this)?.value ?: return 1f
        val aspectRatioStr = find.substring(find.lastIndexOf("=") + 1).replace(",", ".")
        return aspectRatioStr.toFloatOrNull() ?: 1f
    }

    companion object {
        private const val ATTR_ATTACHMENT_TYPE = "attachmentType"
        private const val ATTR_ATTACHMENT_ID = "attachmentID"
        private const val ATTR_ASPECT_RATIO = "aspectRatio"
    }
}