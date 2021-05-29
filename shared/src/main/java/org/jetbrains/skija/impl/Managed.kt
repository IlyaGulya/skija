package org.jetbrains.skija.impl

import java.lang.ref.Cleaner
import java.lang.ref.Cleaner.Cleanable

abstract class Managed @JvmOverloads constructor(
    ptr: Long,
    finalizer: Long,
    managed: Boolean = true
) : Native(ptr),
    AutoCloseable {
    internal var cleanable: Cleanable? = null
    override fun close() {
        when {
            0L == ptr -> throw RuntimeException("Object already closed: $javaClass, ptr=$ptr")
            null == cleanable -> throw RuntimeException(
                "Object is not managed in JVM, can't close(): $javaClass, ptr=$ptr"
            )
            else -> {
                cleanable!!.clean()
                cleanable = null
                ptr = 0
            }
        }
    }

    open val isClosed: Boolean
        get() = ptr == 0L

    class CleanerThunk(
        var _className: String,
        var ptr: Long,
        var _finalizerPtr: Long
    ) : Runnable {
        override fun run() {
            Log.trace { "Cleaning " + _className + " " + ptr.toString(16) }
            Stats.onDeallocated(_className)
            Stats.onNativeCall()
            _nInvokeFinalizer(_finalizerPtr, ptr)
        }
    }

    companion object {
        var _cleaner = Cleaner.create()
        external fun _nInvokeFinalizer(finalizer: Long, ptr: Long)
    }

    init {
        if (managed) {
            assert(ptr != 0L) { "Managed ptr is 0" }
            assert(finalizer != 0L) { "Managed finalizer is 0" }
            val className = javaClass.simpleName
            Stats.onAllocated(className)
            cleanable = _cleaner.register(this, CleanerThunk(className, ptr, finalizer))
        }
    }
}