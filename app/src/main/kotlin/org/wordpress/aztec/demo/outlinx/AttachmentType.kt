package org.wordpress.aztec.demo.outlinx

import org.wordpress.aztec.demo.outlinx.tag.OutlinxTag

enum class AttachmentType(val type: String) {
    WEBLINK("weblink"),
    IMAGE("image"),
    VIDEO("video");

    companion object {

        fun fromString(input: String): AttachmentType? {
            for (attr in values()) {
                if (attr.type == input) return attr
            }
            return null
        }

        fun fromOutlinxTag(input: String): AttachmentType? {
            return if (input.contains(OutlinxTag.TAG_PREFIX)) {
                fromString(input.replace(OutlinxTag.TAG_PREFIX, ""))
            } else null
        }
    }
}