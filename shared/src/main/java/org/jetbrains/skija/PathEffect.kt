package org.jetbrains.skija

import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.RefCnt
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

class PathEffect internal constructor(
    ptr: Long,
) : RefCnt(ptr) {
    companion object {
        fun makePath1D(path: Path?, advance: Float, phase: Float, style: Style): PathEffect {
            return try {
                Stats.onNativeCall()
                PathEffect(
                    _nMakePath1D(
                        getPtr(path),
                        advance,
                        phase,
                        style.ordinal
                    )
                )
            } finally {
                Reference.reachabilityFence(path)
            }
        }

        fun makePath2D(matrix: Matrix33, path: Path?): PathEffect {
            return try {
                Stats.onNativeCall()
                PathEffect(
                    _nMakePath2D(
                        matrix.mat,
                        getPtr(path)
                    )
                )
            } finally {
                Reference.reachabilityFence(path)
            }
        }

        fun makeLine2D(width: Float, matrix: Matrix33): PathEffect {
            Stats.onNativeCall()
            return PathEffect(_nMakeLine2D(width, matrix.mat))
        }

        fun makeCorner(radius: Float): PathEffect {
            Stats.onNativeCall()
            return PathEffect(_nMakeCorner(radius))
        }

        fun makeDash(intervals: FloatArray?, phase: Float): PathEffect {
            Stats.onNativeCall()
            return PathEffect(_nMakeDash(intervals, phase))
        }

        fun makeDiscrete(segLength: Float, dev: Float, seed: Int): PathEffect {
            Stats.onNativeCall()
            return PathEffect(_nMakeDiscrete(segLength, dev, seed))
        }

        external fun _nMakeSum(firstPtr: Long, secondPtr: Long): Long
        external fun _nMakeCompose(outerPtr: Long, innerPtr: Long): Long
        external fun _nComputeFastBounds(ptr: Long, l: Float, t: Float, r: Float, b: Float): Rect
        external fun _nMakePath1D(pathPtr: Long, advance: Float, phase: Float, style: Int): Long
        external fun _nMakePath2D(matrix: FloatArray?, pathPtr: Long): Long
        external fun _nMakeLine2D(width: Float, matrix: FloatArray?): Long
        external fun _nMakeCorner(radius: Float): Long
        external fun _nMakeDash(intervals: FloatArray?, phase: Float): Long
        external fun _nMakeDiscrete(segLength: Float, dev: Float, seed: Int): Long

        init {
            Library.staticLoad()
        }
    }

    enum class Style {
        /** translate the shape to each position  */
        TRANSLATE,

        /** rotate the shape about its center  */
        ROTATE,

        /** transform each point, and turn lines into curves  */
        MORPH
    }

    fun makeSum(second: PathEffect?): PathEffect {
        return try {
            Stats.onNativeCall()
            PathEffect(_nMakeSum(ptr, getPtr(second)))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(second)
        }
    }

    fun makeCompose(inner: PathEffect?): PathEffect {
        return try {
            Stats.onNativeCall()
            PathEffect(_nMakeCompose(ptr, getPtr(inner)))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(inner)
        }
    }

    fun computeFastBounds(src: Rect): Rect {
        return try {
            Stats.onNativeCall()
            _nComputeFastBounds(ptr, src.left, src.top, src.right, src.bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }
}