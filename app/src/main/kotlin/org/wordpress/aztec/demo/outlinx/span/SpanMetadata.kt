package org.wordpress.aztec.demo.outlinx.span

import org.wordpress.aztec.demo.outlinx.AttachmentType

data class SpanMetadata(
        val attributeType: AttachmentType,
        val attributeId: String,
        val imageId: String,
        val aspectRatio: Float
)