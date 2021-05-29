package org.jetbrains.skija

import kotlin.math.roundToInt

data class Color4f(
    val r: Float = 0f,
    val g: Float = 0f,
    val b: Float = 0f,
    val a: Float = 0f,
) {
    constructor(r: Float, g: Float, b: Float) : this(r, g, b, 1f)
    constructor(rgba: FloatArray) : this(rgba[0], rgba[1], rgba[2], rgba[3])
    constructor(c: Int) : this(
        (c shr 16 and 0xFF) / 255f,
        (c shr 8 and 0xFF) / 255f,
        (c and 0xFF) / 255f,
        (c shr 24 and 0xFF) / 255f
    )

    fun toColor(): Int {
        return ((a * 255f).roundToInt() shl 24
                or ((r * 255f).roundToInt() shl 16)
                or ((g * 255f).roundToInt() shl 8)
                or (b * 255f).roundToInt())
    }

    fun flatten(): FloatArray {
        return floatArrayOf(r, g, b, a)
    }

    // TODO premultiply alpha
    fun makeLerp(other: Color4f, weight: Float): Color4f {
        return Color4f(
            r + (other.r - r) * weight,
            g + (other.g - g) * weight,
            b + (other.b - b) * weight,
            a + (other.a - a) * weight
        )
    }

    companion object {
        fun flattenArray(colors: Array<Color4f>): FloatArray {
            val arr = FloatArray(colors.size * 4)
            for (i in colors.indices) {
                arr[i * 4] = colors[i].r
                arr[i * 4 + 1] = colors[i].g
                arr[i * 4 + 2] = colors[i].b
                arr[i * 4 + 3] = colors[i].a
            }
            return arr
        }
    }
}