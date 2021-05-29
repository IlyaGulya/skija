package org.jetbrains.skija

import org.jetbrains.skija.impl.*
import java.lang.ref.Reference

class Region : Managed(_nMake(), _FinalizerHolder.PTR) {
    companion object {
        external fun _nMake(): Long
        external fun _nGetFinalizer(): Long
        external fun _nSet(ptr: Long, regoinPtr: Long): Boolean
        external fun _nIsEmpty(ptr: Long): Boolean
        external fun _nIsRect(ptr: Long): Boolean
        external fun _nIsComplex(ptr: Long): Boolean
        external fun _nGetBounds(ptr: Long): IRect
        external fun _nComputeRegionComplexity(ptr: Long): Int
        external fun _nGetBoundaryPath(ptr: Long, pathPtr: Long): Boolean
        external fun _nSetEmpty(ptr: Long): Boolean
        external fun _nSetRect(ptr: Long, left: Int, top: Int, right: Int, bottom: Int): Boolean
        external fun _nSetRects(ptr: Long, rects: IntArray?): Boolean
        external fun _nSetRegion(ptr: Long, regionPtr: Long): Boolean
        external fun _nSetPath(ptr: Long, pathPtr: Long, regionPtr: Long): Boolean
        external fun _nIntersectsIRect(ptr: Long, left: Int, top: Int, right: Int, bottom: Int): Boolean
        external fun _nIntersectsRegion(ptr: Long, regionPtr: Long): Boolean
        external fun _nContainsIPoint(ptr: Long, x: Int, y: Int): Boolean
        external fun _nContainsIRect(ptr: Long, left: Int, top: Int, right: Int, bottom: Int): Boolean
        external fun _nContainsRegion(ptr: Long, regionPtr: Long): Boolean
        external fun _nQuickContains(ptr: Long, left: Int, top: Int, right: Int, bottom: Int): Boolean
        external fun _nQuickRejectIRect(ptr: Long, left: Int, top: Int, right: Int, bottom: Int): Boolean
        external fun _nQuickRejectRegion(ptr: Long, regionPtr: Long): Boolean
        external fun _nTranslate(ptr: Long, dx: Int, dy: Int)
        external fun _nOpIRect(ptr: Long, left: Int, top: Int, right: Int, bottom: Int, op: Int): Boolean
        external fun _nOpRegion(ptr: Long, regionPtr: Long, op: Int): Boolean
        external fun _nOpIRectRegion(
            ptr: Long,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            regionPtr: Long,
            op: Int
        ): Boolean

        external fun _nOpRegionIRect(
            ptr: Long,
            regionPtr: Long,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            op: Int
        ): Boolean

        external fun _nOpRegionRegion(ptr: Long, regionPtrA: Long, regionPtrB: Long, op: Int): Boolean

        init {
            Library.staticLoad()
        }
    }

    enum class Op {
        DIFFERENCE,
        INTERSECT,
        UNION, XOR,
        REVERSE_DIFFERENCE,
        REPLACE,
    }

    fun set(r: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nSet(ptr, getPtr(r))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    val isEmpty: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsEmpty(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    val isRect: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsRect(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    val isComplex: Boolean
        get() = try {
            Stats.onNativeCall()
            _nIsComplex(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    val bounds: IRect
        get() = try {
            Stats.onNativeCall()
            _nGetBounds(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    fun computeRegionComplexity(): Int {
        return try {
            Stats.onNativeCall()
            _nComputeRegionComplexity(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun getBoundaryPath(p: Path?): Boolean {
        return try {
            Stats.onNativeCall()
            _nGetBoundaryPath(
                ptr,
                getPtr(p)
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(p)
        }
    }

    fun setEmpty(): Boolean {
        return try {
            Stats.onNativeCall()
            _nSetEmpty(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun setRect(rect: IRect): Boolean {
        return try {
            Stats.onNativeCall()
            _nSetRect(ptr, rect._left, rect._top, rect._right, rect._bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun setRects(rects: Array<IRect>): Boolean {
        return try {
            val arr = IntArray(rects.size * 4)
            for (i in rects.indices) {
                arr[i * 4] = rects[i]._left
                arr[i * 4 + 1] = rects[i]._top
                arr[i * 4 + 2] = rects[i]._right
                arr[i * 4 + 3] = rects[i]._bottom
            }
            Stats.onNativeCall()
            _nSetRects(ptr, arr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun setRegion(r: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nSetRegion(ptr, getPtr(r))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun setPath(path: Path?, clip: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nSetPath(
                ptr,
                getPtr(path),
                getPtr(clip)
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(path)
            Reference.reachabilityFence(clip)
        }
    }

    fun intersects(rect: IRect): Boolean {
        return try {
            Stats.onNativeCall()
            _nIntersectsIRect(ptr, rect._left, rect._top, rect._right, rect._bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun intersects(r: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nIntersectsRegion(
                ptr,
                getPtr(r)
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun contains(x: Int, y: Int): Boolean {
        return try {
            Stats.onNativeCall()
            _nContainsIPoint(ptr, x, y)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    operator fun contains(rect: IRect): Boolean {
        return try {
            Stats.onNativeCall()
            _nContainsIRect(ptr, rect._left, rect._top, rect._right, rect._bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    operator fun contains(r: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nContainsRegion(ptr, getPtr(r))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun quickContains(rect: IRect): Boolean {
        return try {
            Stats.onNativeCall()
            _nQuickContains(ptr, rect._left, rect._top, rect._right, rect._bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun quickReject(rect: IRect): Boolean {
        return try {
            Stats.onNativeCall()
            _nQuickRejectIRect(ptr, rect._left, rect._top, rect._right, rect._bottom)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun quickReject(r: Region?): Boolean {
        return try {
            Stats.onNativeCall()
            _nQuickRejectRegion(
                ptr,
                getPtr(r)
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun translate(dx: Int, dy: Int) {
        try {
            Stats.onNativeCall()
            _nTranslate(ptr, dx, dy)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun op(rect: IRect, op: Op): Boolean {
        return try {
            Stats.onNativeCall()
            _nOpIRect(
                ptr,
                rect._left,
                rect._top,
                rect._right,
                rect._bottom,
                op.ordinal
            )
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun op(r: Region?, op: Op): Boolean {
        return try {
            Stats.onNativeCall()
            _nOpRegion(
                ptr,
                getPtr(r),
                op.ordinal
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun op(rect: IRect, r: Region?, op: Op): Boolean {
        return try {
            Stats.onNativeCall()
            _nOpIRectRegion(
                ptr,
                rect._left,
                rect._top,
                rect._right,
                rect._bottom,
                getPtr(r),
                op.ordinal
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun op(r: Region?, rect: IRect, op: Op): Boolean {
        return try {
            Stats.onNativeCall()
            _nOpRegionIRect(
                ptr,
                getPtr(r),
                rect._left,
                rect._top,
                rect._right,
                rect._bottom,
                op.ordinal
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(r)
        }
    }

    fun op(a: Region?, b: Region?, op: Op): Boolean {
        return try {
            Stats.onNativeCall()
            _nOpRegionRegion(
                ptr,
                getPtr(a),
                getPtr(b),
                op.ordinal
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(a)
            Reference.reachabilityFence(b)
        }
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }

    init {
        Stats.onNativeCall()
    }
}