package org.jetbrains.skija

import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

class PathSegmentIterator constructor(
    val path: Path?,
    ptr: Long,
) : Managed(ptr, _nGetFinalizer()),
    Iterator<PathSegment?> {
    companion object {
        fun make(path: Path?, forceClose: Boolean): PathSegmentIterator {
            return try {
                val ptr = _nMake(getPtr(path), forceClose)
                val i = PathSegmentIterator(path, ptr)
                i.nextSegment = _nNext(ptr)
                i
            } finally {
                Reference.reachabilityFence(path)
            }
        }

        external fun _nMake(pathPtr: Long, forceClose: Boolean): Long
        external fun _nGetFinalizer(): Long
        external fun _nNext(ptr: Long): PathSegment?

        init {
            Library.staticLoad()
        }
    }

    var nextSegment: PathSegment? = null
    override fun next(): PathSegment? {
        return try {
            if (nextSegment!!.verb == PathVerb.DONE) throw NoSuchElementException()
            val res = nextSegment
            nextSegment = _nNext(ptr)
            res
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    override fun hasNext(): Boolean {
        return nextSegment!!.verb != PathVerb.DONE
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }

    init {
        Stats.onNativeCall()
    }
}