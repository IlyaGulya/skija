package org.jetbrains.skija

import org.jetbrains.annotations.ApiStatus.Internal
import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

class TextBlob @Internal constructor(ptr: Long) : Managed(ptr, _FinalizerHolder.PTR) {
    companion object {
        /**
         * Returns a TextBlob built from a single run of text with x-positions and a single y value.
         * Returns null if glyphs is empty.
         *
         * @param glyphs  glyphs drawn
         * @param xpos    array of x-positions, must contain values for all of the glyphs.
         * @param ypos    shared y-position for each glyph, to be paired with each xpos.
         * @param font    Font used for this run
         * @return        new TextBlob or null
         */
        fun makeFromPosH(glyphs: ShortArray, xpos: FloatArray, ypos: Float, font: Font?): TextBlob? {
            return try {
                assert(glyphs.size == xpos.size) { "glyphs.length " + glyphs.size + " != xpos.length " + xpos.size }
                Stats.onNativeCall()
                val ptr = _nMakeFromPosH(glyphs, xpos, ypos, getPtr(font))
                if (ptr == 0L) null else TextBlob(ptr)
            } finally {
                Reference.reachabilityFence(font)
            }
        }

        /**
         * Returns a TextBlob built from a single run of text with positions.
         * Returns null if glyphs is empty.
         *
         * @param glyphs  glyphs drawn
         * @param pos     array of positions, must contain values for all of the glyphs.
         * @param font    Font used for this run
         * @return        new TextBlob or null
         */
        fun makeFromPos(glyphs: ShortArray, pos: Array<Point>, font: Font?): TextBlob? {
            return try {
                assert(glyphs.size == pos.size) { "glyphs.length " + glyphs.size + " != pos.length " + pos.size }
                val floatPos = FloatArray(pos.size * 2)
                for (i in pos.indices) {
                    floatPos[i * 2] = pos[i]._x
                    floatPos[i * 2 + 1] = pos[i]._y
                }
                Stats.onNativeCall()
                val ptr = _nMakeFromPos(glyphs, floatPos, getPtr(font))
                if (ptr == 0L) null else TextBlob(ptr)
            } finally {
                Reference.reachabilityFence(font)
            }
        }

        fun makeFromRSXform(glyphs: ShortArray, xform: Array<RSXform>, font: Font?): TextBlob? {
            return try {
                assert(glyphs.size == xform.size) { "glyphs.length " + glyphs.size + " != xform.length " + xform.size }
                val floatXform = FloatArray(xform.size * 4)
                for (i in xform.indices) {
                    floatXform[i * 4] = xform[i]._scos
                    floatXform[i * 4 + 1] = xform[i]._ssin
                    floatXform[i * 4 + 2] = xform[i]._tx
                    floatXform[i * 4 + 3] = xform[i]._ty
                }
                Stats.onNativeCall()
                val ptr = _nMakeFromRSXform(glyphs, floatXform, getPtr(font))
                if (ptr == 0L) null else TextBlob(ptr)
            } finally {
                Reference.reachabilityFence(font)
            }
        }

        fun makeFromData(data: Data?): TextBlob? {
            return try {
                Stats.onNativeCall()
                val ptr = _nMakeFromData(getPtr(data))
                if (ptr == 0L) null else TextBlob(ptr)
            } finally {
                Reference.reachabilityFence(data)
            }
        }

        internal external fun _nGetFinalizer(): Long
        internal external fun _nBounds(ptr: Long): Rect
        internal external fun _nGetUniqueId(ptr: Long): Int
        internal external fun _nGetIntercepts(ptr: Long, lower: Float, upper: Float, paintPtr: Long): FloatArray
        internal external fun _nMakeFromPosH(glyphs: ShortArray?, xpos: FloatArray?, ypos: Float, fontPtr: Long): Long
        internal external fun _nMakeFromPos(glyphs: ShortArray?, pos: FloatArray?, fontPtr: Long): Long
        internal external fun _nMakeFromRSXform(glyphs: ShortArray?, xform: FloatArray?, fontPtr: Long): Long
        internal external fun _nSerializeToData(ptr: Long /*, SkSerialProcs */): Long
        internal external fun _nMakeFromData(dataPtr: Long /*, SkDeserialProcs */): Long
        internal external fun _nGetGlyphs(ptr: Long): ShortArray
        internal external fun _nGetPositions(ptr: Long): FloatArray
        internal external fun _nGetClusters(ptr: Long): IntArray?
        internal external fun _nGetTightBounds(ptr: Long): Rect
        internal external fun _nGetBlockBounds(ptr: Long): Rect
        internal external fun _nGetFirstBaseline(ptr: Long): Float
        internal external fun _nGetLastBaseline(ptr: Long): Float

        init {
            Library.staticLoad()
        }
    }

    /**
     * Returns conservative bounding box. Uses Paint associated with each glyph to
     * determine glyph bounds, and unions all bounds. Returned bounds may be
     * larger than the bounds of all glyphs in runs.
     *
     * @return  conservative bounding box
     */
    val bounds: Rect
        get() = try {
            Stats.onNativeCall()
            _nBounds(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Returns a non-zero value unique among all text blobs.
     *
     * @return  identifier for TextBlob
     */
    val uniqueId: Int
        get() = try {
            Stats.onNativeCall()
            _nGetUniqueId(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     *
     * Returns the number of intervals that intersect bounds.
     * bounds describes a pair of lines parallel to the text advance.
     * The return array size is a multiple of two, and is at most twice the number of glyphs in
     * the the blob.
     *
     *
     * Runs within the blob that contain SkRSXform are ignored when computing intercepts.
     *
     * @param lowerBound lower line parallel to the advance
     * @param upperBound upper line parallel to the advance
     * @return           intersections; may be null
     */
    fun getIntercepts(lowerBound: Float, upperBound: Float): FloatArray {
        return getIntercepts(lowerBound, upperBound)
    }

    /**
     *
     * Returns the number of intervals that intersect bounds.
     * bounds describes a pair of lines parallel to the text advance.
     * The return array size is a multiple of two, and is at most twice the number of glyphs in
     * the the blob.
     *
     *
     * Runs within the blob that contain SkRSXform are ignored when computing intercepts.
     *
     * @param lowerBound lower line parallel to the advance
     * @param upperBound upper line parallel to the advance
     * @param paint      specifies stroking, PathEffect that affects the result; may be null
     * @return           intersections; may be null
     */
    fun getIntercepts(lowerBound: Float, upperBound: Float, paint: Paint?): FloatArray {
        return try {
            Stats.onNativeCall()
            _nGetIntercepts(ptr, lowerBound, upperBound, getPtr(paint))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(paint)
        }
    }

    fun serializeToData(): Data {
        return try {
            Stats.onNativeCall()
            Data(_nSerializeToData(ptr))
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * @return  glyph indices for the whole blob
     */
    val glyphs: ShortArray
        get() = try {
            Stats.onNativeCall()
            _nGetGlyphs(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     *
     * Return result depends on how blob was constructed.
     *
     *  * makeFromPosH returns 1 float per glyph (x pos)
     *  * makeFromPos returns 2 floats per glyph (x, y pos)
     *  * makeFromRSXform returns 4 floats per glyph
     *
     *
     *
     * Blobs constructed by TextBlobBuilderRunHandler/Shaper default handler have 2 floats per glyph.
     *
     * @return  glyph positions for the blob if it was made with makeFromPos, null otherwise
     */
    val positions: FloatArray
        get() = try {
            Stats.onNativeCall()
            _nGetPositions(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Only works on TextBlobs that come from TextBlobBuilderRunHandler/Shaper default handler.
     *
     * @return  utf-16 offsets of clusters that start the glyph
     * @throws  IllegalArgumentException if TextBlob doesn’t have this information
     */
    val clusters: IntArray
        get() = try {
            Stats.onNativeCall()
            val res = _nGetClusters(ptr) ?: throw IllegalArgumentException()
            res
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Only works on TextBlobs that come from TextBlobBuilderRunHandler/Shaper default handler.
     *
     * @return  tight bounds around all the glyphs in the TextBlob
     * @throws  IllegalArgumentException if TextBlob doesn’t have this information
     */
    val tightBounds: Rect
        get() = try {
            Stats.onNativeCall()
            val res = _nGetTightBounds(ptr) ?: throw IllegalArgumentException()
            res
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Only works on TextBlobs that come from TextBlobBuilderRunHandler/Shaper default handler.
     *
     * @return  tight bounds around all the glyphs in the TextBlob
     * @throws  IllegalArgumentException if TextBlob doesn’t have this information
     */
    val blockBounds: Rect
        get() = try {
            Stats.onNativeCall()
            val res = _nGetBlockBounds(ptr) ?: throw IllegalArgumentException()
            res
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Only works on TextBlobs that come from TextBlobBuilderRunHandler/Shaper default handler.
     *
     * @return  first baseline in TextBlob
     * @throws  IllegalArgumentException if TextBlob doesn’t have this information
     */
    val firstBaseline: Float
        get() = try {
            Stats.onNativeCall()
            val res = _nGetFirstBaseline(ptr) ?: throw IllegalArgumentException()
            res
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     * Only works on TextBlobs that come from TextBlobBuilderRunHandler/Shaper default handler.
     *
     * @return  last baseline in TextBlob
     * @throws  IllegalArgumentException if TextBlob doesn’t have this information
     */
    val lastBaseline: Float
        get() = try {
            Stats.onNativeCall()
            val res = _nGetLastBaseline(ptr) ?: throw IllegalArgumentException()
            res
        } finally {
            Reference.reachabilityFence(this)
        }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }
}