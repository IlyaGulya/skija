package org.jetbrains.skija

import kotlin.math.abs

data class FontExtents(
    val ascender: Float,
    val descender: Float,
    val lineGap: Float,
) {

    val ascenderAbs: Float
        get() = abs(ascender)
    val lineHeight: Float
        get() = -ascender + descender + lineGap
}