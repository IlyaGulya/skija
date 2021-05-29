package org.jetbrains.skija

import org.jetbrains.skija.impl.*
import java.lang.ref.Reference

class ColorSpace : Managed {
    companion object {
        external fun _nGetFinalizer(): Long
        external fun _nMakeSRGB(): Long
        external fun _nMakeDisplayP3(): Long
        external fun _nMakeSRGBLinear(): Long
        external fun _nConvert(fromPtr: Long, toPtr: Long, r: Float, g: Float, b: Float, a: Float): FloatArray
        external fun _nIsGammaCloseToSRGB(ptr: Long): Boolean
        external fun _nIsGammaLinear(ptr: Long): Boolean
        external fun _nIsSRGB(ptr: Long): Boolean

        val sRGB: ColorSpace
            get() = SRGBHolder.sRGB

        init {
            Library.staticLoad()
        }
    }

    object SRGBHolder {
        val sRGB = ColorSpace(_nMakeSRGB(), false)

        init {
            Stats.onNativeCall()
        }
    }

    object SRGBLinearHolder {
        val sRGBLinear = ColorSpace(_nMakeSRGBLinear(), false)

        init {
            Stats.onNativeCall()
        }
    }

    object DisplayP3Holder {
        val displayP3 = ColorSpace(_nMakeDisplayP3(), false)

        init {
            Stats.onNativeCall()
        }
    }

    fun convert(to: ColorSpace?, color: Color4f): Color4f {
        val to = to ?: sRGB
        return try {
            Color4f(
                _nConvert(
                    ptr,
                    getPtr(to),
                    color.r,
                    color.g,
                    color.b,
                    color.a
                )
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(to)
        }
    }

    internal constructor(ptr: Long) : super(ptr, _FinalizerHolder.PTR, true) {
    }

    internal constructor(ptr: Long, managed: Boolean) : super(ptr, _FinalizerHolder.PTR, managed) {
    }

    /**
     * @return  true if the color space gamma is near enough to be approximated as sRGB
     */
    val isGammaCloseToSRGB: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsGammaCloseToSRGB(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * @return  true if the color space gamma is linear
     */
    val isGammaLinear: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsGammaLinear(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     *
     * Returns true if the color space is sRGB. Returns false otherwise.
     *
     *
     * This allows a little bit of tolerance, given that we might see small numerical error
     * in some cases: converting ICC fixed point to float, converting white point to D50,
     * rounding decisions on transfer function and matrix.
     *
     *
     * This does not consider a 2.2f exponential transfer function to be sRGB.  While these
     * functions are similar (and it is sometimes useful to consider them together), this
     * function checks for logical equality.
     */
    val isSRGB: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsSRGB(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }
}