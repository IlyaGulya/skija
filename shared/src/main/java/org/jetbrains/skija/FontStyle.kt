package org.jetbrains.skija

data class FontStyle(
    val value: Int,
) {

    constructor(weight: Int, width: Int, slant: FontSlant) : this(
        value = weight and 0xFFFF or (width and 0xFF shl 16) or (slant.ordinal shl 24)
    )

    val weight: Int
        get() = value and 0xFFFF

    fun withWeight(weight: Int): FontStyle {
        return FontStyle(weight, width, slant)
    }

    val width: Int
        get() = value shr 16 and 0xFF

    fun withWidth(width: Int): FontStyle {
        return FontStyle(weight, width, slant)
    }

    val slant: FontSlant
        get() = FontSlant.values()[value shr 24 and 0xFF]

    fun withSlant(slant: FontSlant): FontStyle {
        return FontStyle(weight, width, slant)
    }

    override fun toString(): String {
        return "FontStyle(" +
                "weight=" + weight +
                ", width=" + width +
                ", slant='" + slant + ')'
    }

    companion object {
        val NORMAL = FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.UPRIGHT)
        val BOLD = FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.UPRIGHT)
        val ITALIC = FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.ITALIC)
        val BOLD_ITALIC = FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.ITALIC)
    }
}