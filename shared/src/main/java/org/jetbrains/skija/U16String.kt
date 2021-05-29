package org.jetbrains.skija

import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

/**
 * Java mirror of std::vector&lt;jchar&gt; (UTF-16)
 */
class U16String internal constructor(
    ptr: Long
) : Managed(ptr, _FinalizerHolder.PTR) {
    companion object {
        internal external fun _nMake(s: String?): Long

        internal external fun _nGetFinalizer(): Long

        internal external fun _nToString(ptr: Long): String

        init {
            Library.staticLoad()
        }
    }

    constructor(s: String?) : this(_nMake(s)) {
        Stats.onNativeCall()
    }

    override fun toString(): String {
        return try {
            Stats.onNativeCall()
            _nToString(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }
}