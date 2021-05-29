package org.jetbrains.skija

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import java.util.regex.Pattern

data class FontFeature(
    private val _tag: Int,
    val value: Int,
    val start: Long,
    val end: Long,
) {

    constructor(feature: String, value: Int, start: Long, end: Long) : this(
        _tag = FourByteTag.fromString(feature),
        value = value,
        start = start,
        end = end
    )

    constructor(feature: String, value: Int) : this(
        _tag = FourByteTag.fromString(feature),
        value = value,
        start = GLOBAL_START,
        end = GLOBAL_END
    )

    constructor(feature: String, value: Boolean) : this(
        _tag = FourByteTag.fromString(feature),
        value = if (value) 1 else 0,
        start = GLOBAL_START,
        end = GLOBAL_END
    )

    constructor(feature: String) : this(
        _tag = FourByteTag.fromString(feature),
        value = 1,
        start = GLOBAL_START,
        end = GLOBAL_END
    )

    val tag: String
        get() = FourByteTag.toString(_tag)

    override fun toString(): String {
        var range = ""
        if (start > 0 || end < Long.MAX_VALUE) {
            range = "[" + (if (start > 0) start else "") + ":" + (if (end < Long.MAX_VALUE) end else "") + "]"
        }
        var valuePrefix = ""
        var valueSuffix = ""
        if (value == 0) valuePrefix = "-" else if (value == 1) valuePrefix = "+" else valueSuffix = "=$value"
        return "FontFeature($valuePrefix$tag$range$valueSuffix)"
    }

    companion object {
        const val GLOBAL_START: Long = 0
        const val GLOBAL_END = Long.MAX_VALUE
        val EMPTY = arrayOfNulls<FontFeature>(0)
        val _splitPattern = Pattern.compile("\\s+")
        val _featurePattern =
            Pattern.compile("(?<sign>[-+])?(?<tag>[a-z0-9]{4})(?:\\[(?<start>\\d+)?:(?<end>\\d+)?\\])?(?:=(?<value>\\d+))?")

        fun parseOne(s: String): FontFeature {
            val m = _featurePattern.matcher(s)
            require(m.matches()) { "Can’t parse FontFeature: $s" }
            val value = if (m.group("value") != null) m.group("value")
                .toInt() else if (m.group("sign") == null) 1 else if ("-" == m.group("sign")) 0 else 1
            val start = if (m.group("start") == null) 0 else m.group("start").toLong()
            val end = if (m.group("end") == null) Long.MAX_VALUE else m.group("end").toLong()
            return FontFeature(m.group("tag"), value, start, end)
        }

        fun parse(s: String?): Array<FontFeature> {
            return _splitPattern
                .split(s)
                .map { parseOne(it) }
                .toTypedArray()
        }
    }
}