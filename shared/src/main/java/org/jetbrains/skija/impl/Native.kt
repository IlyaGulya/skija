package org.jetbrains.skija.impl

import java.lang.ref.Reference

abstract class Native(
    val ptr: Long
) {
    init {
        require(ptr != 0L) { "Can't wrap nullptr" }
    }

    override fun toString(): String {
        return javaClass.simpleName + "(ptr=0x${ptr.toString(16)})"
    }

    override fun equals(other: Any?): Boolean {
        return try {
            if (this === other) return true
            if (null == other) return false
            if (!javaClass.isInstance(other)) return false
            val nOther = other as Native
            if (ptr == nOther.ptr) true else _nativeEquals(nOther)
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(other)
        }
    }

    internal open fun _nativeEquals(other: Native?): Boolean {
        return false
    }

    // FIXME two different pointers might point to equal objects
    override fun hashCode(): Int {
        return ptr.hashCode()
    }

    companion object {
        fun getPtr(n: Native?): Long {
            return n?.ptr ?: 0
        }
    }

}