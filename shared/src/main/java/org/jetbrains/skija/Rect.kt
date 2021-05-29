package org.jetbrains.skija

import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.ToString
import org.jetbrains.annotations.ApiStatus.Internal
import org.jetbrains.annotations.Contract
import kotlin.math.max
import kotlin.math.min

@Getter
@EqualsAndHashCode
@ToString
open class Rect @Internal constructor(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    val width: Float
        get() = right - left
    val height: Float
        get() = bottom - top

    fun intersect(other: Rect?): Rect? {
        requireNotNull(other) { "Rect::intersect expected other != null" }

        if (right <= other.left
            || other.right <= left
            || bottom <= other.top
            || other.bottom <= top
        ) return null

        return Rect(
            max(left, other.left),
            max(top, other.top),
            min(right, other.right),
            min(bottom, other.bottom)
        )
    }

    fun scale(scale: Float): Rect {
        return scale(scale, scale)
    }

    fun scale(sx: Float, sy: Float): Rect {
        return Rect(left * sx, top * sy, right * sx, bottom * sy)
    }

    fun offset(dx: Float, dy: Float): Rect {
        return Rect(left + dx, top + dy, right + dx, bottom + dy)
    }

    fun offset(vec: Point?): Rect {
        requireNotNull(vec) { "Rect::offset expected vec != null" }
        return offset(vec._x, vec._y)
    }

    @Contract("-> new")
    fun toIRect(): IRect {
        return IRect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    open fun inflate(spread: Float): Rect {
        return if (spread <= 0) makeLTRB(
            left - spread,
            top - spread,
            max(left - spread, right + spread),
            max(top - spread, bottom + spread)
        ) else RRect.Companion.makeLTRB(
            left - spread,
            top - spread,
            max(left - spread, right + spread),
            max(top - spread, bottom + spread),
            spread
        )
    }

    val isEmpty: Boolean
        get() = right == left || top == bottom

    companion object {
        @Contract("_, _, _, _ -> new")
        fun makeLTRB(l: Float, t: Float, r: Float, b: Float): Rect {
            require(l <= r) { "Rect::makeLTRB expected l <= r, got $l > $r" }
            require(t <= b) { "Rect::makeLTRB expected t <= b, got $t > $b" }
            return Rect(l, t, r, b)
        }

        @Contract("_, _ -> new")
        fun makeWH(w: Float, h: Float): Rect {
            require(w >= 0) { "Rect::makeWH expected w >= 0, got: $w" }
            require(h >= 0) { "Rect::makeWH expected h >= 0, got: $h" }
            return Rect(0f, 0f, w, h)
        }

        @Contract("_, _ -> new")
        fun makeWH(size: Point?): Rect {
            requireNotNull(size) { "Rect::makeWH expected size != null" }
            return makeWH(size._x, size._y)
        }

        @Contract("_, _, _, _ -> new")
        fun makeXYWH(l: Float, t: Float, w: Float, h: Float): Rect {
            require(w >= 0) { "Rect::makeXYWH expected w >= 0, got: $w" }
            require(h >= 0) { "Rect::makeXYWH expected h >= 0, got: $h" }
            return Rect(l, t, l + w, t + h)
        }
    }
}