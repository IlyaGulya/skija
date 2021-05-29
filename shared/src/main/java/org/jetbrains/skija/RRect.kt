package org.jetbrains.skija

import lombok.EqualsAndHashCode
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@EqualsAndHashCode(callSuper = true)
class RRect internal constructor(
    l: Float,
    t: Float,
    r: Float,
    b: Float,
    val radii: FloatArray
) : Rect(l, t, r, b) {
    override fun inflate(spread: Float): Rect {
        var becomesRect = true
        for (i in radii.indices) {
            if (radii[i] + spread >= 0) {
                becomesRect = false
                break
            }
        }
        return if (becomesRect) makeLTRB(
            left - spread,
            top - spread,
            max(left - spread, right + spread),
            max(top - spread, bottom + spread)
        ) else {
            val radii = radii.copyOf(radii.size)
            for (i in radii.indices) radii[i] = max(0f, radii[i] + spread)
            RRect(
                left - spread,
                top - spread,
                max(left - spread, right + spread),
                max(top - spread, bottom + spread),
                radii
            )
        }
    }

    override fun toString(): String {
        return "RRect(_left=$left, _top=$top, _right=$right, _bottom=$bottom, _radii=${radii.contentToString()})"
    }

    companion object {
        fun makeLTRB(l: Float, t: Float, r: Float, b: Float, radius: Float): RRect {
            return RRect(l, t, r, b, floatArrayOf(radius))
        }

        fun makeLTRB(l: Float, t: Float, r: Float, b: Float, xRad: Float, yRad: Float): RRect {
            return RRect(l, t, r, b, floatArrayOf(xRad, yRad))
        }

        fun makeLTRB(
            l: Float,
            t: Float,
            r: Float,
            b: Float,
            tlRad: Float,
            trRad: Float,
            brRad: Float,
            blRad: Float
        ): RRect {
            return RRect(l, t, r, b, floatArrayOf(tlRad, trRad, brRad, blRad))
        }

        fun makeNinePatchLTRB(
            l: Float,
            t: Float,
            r: Float,
            b: Float,
            lRad: Float,
            tRad: Float,
            rRad: Float,
            bRad: Float
        ): RRect {
            return RRect(l, t, r, b, floatArrayOf(lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad))
        }

        fun makeComplexLTRB(l: Float, t: Float, r: Float, b: Float, radii: FloatArray): RRect {
            return RRect(l, t, r, b, radii)
        }

        fun makeOvalLTRB(l: Float, t: Float, r: Float, b: Float): RRect {
            return RRect(l, t, r, b, floatArrayOf(abs(r - l) / 2f, abs(b - t) / 2f))
        }

        fun makePillLTRB(l: Float, t: Float, r: Float, b: Float): RRect {
            return RRect(l, t, r, b, floatArrayOf(min(abs(r - l), abs(t - b)) / 2f))
        }

        fun makeXYWH(l: Float, t: Float, w: Float, h: Float, radius: Float): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(radius))
        }

        fun makeXYWH(l: Float, t: Float, w: Float, h: Float, xRad: Float, yRad: Float): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(xRad, yRad))
        }

        fun makeXYWH(
            l: Float,
            t: Float,
            w: Float,
            h: Float,
            tlRad: Float,
            trRad: Float,
            brRad: Float,
            blRad: Float
        ): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(tlRad, trRad, brRad, blRad))
        }

        fun makeNinePatchXYWH(
            l: Float,
            t: Float,
            w: Float,
            h: Float,
            lRad: Float,
            tRad: Float,
            rRad: Float,
            bRad: Float
        ): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad))
        }

        fun makeComplexXYWH(l: Float, t: Float, w: Float, h: Float, radii: FloatArray): RRect {
            return RRect(l, t, l + w, t + h, radii)
        }

        fun makeOvalXYWH(l: Float, t: Float, w: Float, h: Float): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(w / 2f, h / 2f))
        }

        fun makePillXYWH(l: Float, t: Float, w: Float, h: Float): RRect {
            return RRect(l, t, l + w, t + h, floatArrayOf(min(w, h) / 2f))
        }
    }
}