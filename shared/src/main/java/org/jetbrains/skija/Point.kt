package org.jetbrains.skija

import org.jetbrains.annotations.Contract

data class Point(
    internal val _x: Float = 0f,
    internal val _y: Float = 0f,
) {
    fun offset(dx: Float, dy: Float): Point {
        return Point(_x + dx, _y + dy)
    }

    fun offset(vec: Point?): Point {
        requireNotNull(vec) { "Point::offset expected other != null" }
        return offset(vec._x, vec._y)
    }

    fun scale(scale: Float): Point {
        return scale(scale, scale)
    }

    fun scale(sx: Float, sy: Float): Point {
        return Point(_x * sx, _y * sy)
    }

    val isEmpty: Boolean
        get() = _x <= 0 || _y <= 0

    companion object {
        val ZERO = Point(0f, 0f)

        @Contract("null -> null; !null -> new")
        fun flattenArray(pts: Array<Point>?): FloatArray? {
            if (pts == null) return null
            val arr = FloatArray(pts.size * 2)
            for (i in pts.indices) {
                arr[i * 2] = pts[i]._x
                arr[i * 2 + 1] = pts[i]._y
            }
            return arr
        }

        @Contract("null -> null; !null -> new")
        fun fromArray(pts: FloatArray?): Array<Point?>? {
            if (pts == null) return null
            assert(pts.size % 2 == 0) { "Expected " + pts.size + " % 2 == 0" }
            val arr = arrayOfNulls<Point>(pts.size / 2)
            for (i in 0 until pts.size / 2) arr[i] = Point(pts[i * 2], pts[i * 2 + 1])
            return arr
        }
    }
}