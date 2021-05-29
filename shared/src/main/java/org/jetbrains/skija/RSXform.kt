package org.jetbrains.skija

import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * A compressed form of a rotation+scale matrix.
 *
 * <pre>[ fSCos     -fSSin    fTx ]
 * [ fSSin      fSCos    fTy ]
 * [     0          0      1 ]</pre>
 */
data class RSXform(
    internal val scos: Float = 0f,
    internal val ssin: Float = 0f,
    internal val tx: Float = 0f,
    internal val ty: Float = 0f,
) {
    companion object {
        /**
         * Initialize a new xform based on the scale, rotation (in radians), final tx,ty location
         * and anchor-point ax,ay within the src quad.
         *
         * Note: the anchor point is not normalized (e.g. 0...1) but is in pixels of the src image.
         */
        fun makeFromRadians(scale: Float, radians: Float, tx: Float, ty: Float, ax: Float, ay: Float): RSXform {
            val s = sin(radians.toDouble()).toFloat() * scale
            val c = cos(radians.toDouble()).toFloat() * scale
            return RSXform(c, s, tx + -c * ax + s * ay, ty + -s * ax - c * ay)
        }
    }
}