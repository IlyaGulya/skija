package org.jetbrains.skija

import org.jetbrains.annotations.ApiStatus.Internal
import org.jetbrains.annotations.Contract
import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats
import org.jetbrains.skija.shaper.Shaper
import java.lang.ref.Reference

class TextLine @Internal constructor(ptr: Long) : Managed(ptr, _FinalizerHolder.PTR) {
    companion object {
        @Contract("_, _ -> new")
        fun make(text: String?, font: Font?): TextLine {
            return make(text, font, null, true)
        }

        @Contract("_, _, _, _ -> new")
        fun make(text: String?, font: Font?, features: Array<FontFeature?>?, leftToRight: Boolean): TextLine {
            Shaper.makeShapeDontWrapOrReorder()
                .use { shaper -> return shaper.shapeLine(text, font, features, leftToRight) }
        }

        internal external fun _nGetFinalizer(): Long

        internal external fun _nGetAscent(ptr: Long): Float

        internal external fun _nGetCapHeight(ptr: Long): Float

        internal external fun _nGetXHeight(ptr: Long): Float

        internal external fun _nGetDescent(ptr: Long): Float

        internal external fun _nGetLeading(ptr: Long): Float

        internal external fun _nGetWidth(ptr: Long): Float

        internal external fun _nGetHeight(ptr: Long): Float

        internal external fun _nGetTextBlob(ptr: Long): Long

        internal external fun _nGetGlyphs(ptr: Long): ShortArray

        internal external fun _nGetPositions(ptr: Long): FloatArray

        internal external fun _nGetRunPositions(ptr: Long): FloatArray?

        internal external fun _nGetBreakPositions(ptr: Long): FloatArray?

        internal external fun _nGetBreakOffsets(ptr: Long): IntArray?

        internal external fun _nGetOffsetAtCoord(ptr: Long, x: Float): Int

        internal external fun _nGetLeftOffsetAtCoord(ptr: Long, x: Float): Int

        internal external fun _nGetCoordAtOffset(ptr: Long, offset: Int): Float

        init {
            Library.staticLoad()
        }
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }

    val ascent: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetAscent(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val capHeight: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetCapHeight(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val xHeight: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetXHeight(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val descent: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetDescent(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val leading: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetLeading(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val width: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetWidth(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val height: Float
        get() {
            Stats.onNativeCall()
            return try {
                _nGetHeight(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val textBlob: TextBlob?
        get() {
            Stats.onNativeCall()
            return try {
                val res = _nGetTextBlob(ptr)
                if (res == 0L) null else TextBlob(res)
            } finally {
                Reference.reachabilityFence(this)
            }
        }
    val glyphs: ShortArray
        get() {
            Stats.onNativeCall()
            return try {
                _nGetGlyphs(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }

    /**
     * @return  [x0, y0, x1, y1, ...]
     */
    val positions: FloatArray
        get() {
            Stats.onNativeCall()
            return try {
                _nGetPositions(ptr)
            } finally {
                Reference.reachabilityFence(this)
            }
        }

    /**
     * @param x  coordinate in px
     * @return   UTF-16 offset of glyph
     */
    fun getOffsetAtCoord(x: Float): Int {
        return try {
            Stats.onNativeCall()
            _nGetOffsetAtCoord(ptr, x)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * @param x  coordinate in px
     * @return   UTF-16 offset of glyph strictly left of coord
     */
    fun getLeftOffsetAtCoord(x: Float): Int {
        return try {
            Stats.onNativeCall()
            _nGetLeftOffsetAtCoord(ptr, x)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * @param offset  UTF-16 character offset
     * @return        glyph coordinate
     */
    fun getCoordAtOffset(offset: Int): Float {
        return try {
            Stats.onNativeCall()
            _nGetCoordAtOffset(ptr, offset)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     *
     * Returns the number of intervals that intersect bounds.
     * bounds describes a pair of lines parallel to the text advance.
     * The return array size is a multiple of two, and is at most twice the number of glyphs in
     * the the blob.
     *
     * @param lowerBound lower line parallel to the advance
     * @param upperBound upper line parallel to the advance
     * @return           intersections; may be null
     */
    fun getIntercepts(lowerBound: Float, upperBound: Float): FloatArray? {
        return getIntercepts(lowerBound, upperBound, null)
    }

    /**
     *
     * Returns the number of intervals that intersect bounds.
     * bounds describes a pair of lines parallel to the text advance.
     * The return array size is a multiple of two, and is at most twice the number of glyphs in
     * the the blob.
     *
     * @param lowerBound lower line parallel to the advance
     * @param upperBound upper line parallel to the advance
     * @param paint      specifies stroking, PathEffect that affects the result; may be null
     * @return           intersections; may be null
     */
    fun getIntercepts(lowerBound: Float, upperBound: Float, paint: Paint?): FloatArray? {
        try {
            textBlob.use { blob -> return blob?.getIntercepts(lowerBound, upperBound, paint) }
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(paint)
        }
    }
}