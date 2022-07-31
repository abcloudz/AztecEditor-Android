package org.wordpress.aztec.demo.outlinx.tag

object OutlinxTag {
    const val TAG_PREFIX = "outlinx_"
    const val TAG_MEDIA_CENTER = "uwu"

    const val ATTRIBUTE_ID = "id"
    const val ATTRIBUTE_IMAGE_ID = "imageid"
    const val ATTRIBUTE_RATIO = "ratio"

    const val ORIGIN_ATTR_ATTACHMENT_TYPE = "attachmentType"
    const val ORIGIN_ATTR_ATTACHMENT_ID = "attachmentID"
    const val ORIGIN_ATTR_ASPECT_RATIO = "aspectRatio"

    fun buildTag(attachmentType: String, attachmentId: String, imageId: String, aspectRatio: Float): String = buildString {
        append("<")
        append(TAG_PREFIX).append(attachmentType).append(" ")
        append(ATTRIBUTE_ID).append("=").append("\"$attachmentId\"").append(" ")
        append(ATTRIBUTE_IMAGE_ID).append("=").append("\"$imageId\"").append(" ")
        append(ATTRIBUTE_RATIO).append("=").append("\"$aspectRatio\"")

        append("/>")
    }

    //    <img src="image://bAUCdbJHEV?
    //    attachmentType=weblink&
    //    attachmentID=ITG9IXlDDH&
    //    aspectRatio=0,5625"
    //    class="aligncenter size-full">
    fun buildOriginTag(attachmentType: String, attachmentId: String, imageId: String, aspectRatio: Float) = buildString {
        append("<img src=\"image://").append(imageId).append("?")
        append(ORIGIN_ATTR_ATTACHMENT_TYPE).append("=").append(attachmentType).append("&")
        append(ORIGIN_ATTR_ATTACHMENT_ID).append("=").append(attachmentId).append("&")
        val ratio = aspectRatio.toString().replace(".", ",")
        append(ORIGIN_ATTR_ASPECT_RATIO).append("=").append(ratio).append("\" ")
        append("class=\"aligncenter size-full\">")
    }
}