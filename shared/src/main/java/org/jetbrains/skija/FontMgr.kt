package org.jetbrains.skija

import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.RefCnt
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class FontMgr : RefCnt {
    @Suppress("FunctionName", "MemberVisibilityCanBePrivate")
    companion object {
        external fun _nGetFamiliesCount(ptr: Long): Int
        external fun _nGetFamilyName(ptr: Long, index: Int): String
        external fun _nMakeStyleSet(ptr: Long, index: Int): Long
        external fun _nMatchFamily(ptr: Long, familyName: String?): Long
        external fun _nMatchFamilyStyle(ptr: Long, familyName: String?, fontStyle: Int): Long
        external fun _nMatchFamilyStyleCharacter(
            ptr: Long,
            familyName: String?,
            fontStyle: Int,
            bcp47: Array<String?>?,
            character: Int
        ): Long

        external fun _nMakeFromData(ptr: Long, dataPtr: Long, ttcIndex: Int): Long
        external fun _nDefault(): Long

        init {
            Library.staticLoad()
        }
    }

    val familiesCount: Int
        get() = try {
            Stats.onNativeCall()
            _nGetFamiliesCount(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    fun getFamilyName(index: Int): String {
        return try {
            Stats.onNativeCall()
            _nGetFamilyName(ptr, index)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun makeStyleSet(index: Int): FontStyleSet? {
        return try {
            Stats.onNativeCall()
            val ptr = _nMakeStyleSet(ptr, index)
            if (ptr == 0L) null else FontStyleSet(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * The caller must call [.close] on the returned object.
     * Never returns null; will return an empty set if the name is not found.
     *
     * Passing null as the parameter will return the default system family.
     * Note that most systems don't have a default system family, so passing null will often
     * result in the empty set.
     *
     * It is possible that this will return a style set not accessible from
     * [.makeStyleSet] due to hidden or auto-activated fonts.
     */
    fun matchFamily(familyName: String?): FontStyleSet {
        return try {
            Stats.onNativeCall()
            FontStyleSet(_nMatchFamily(ptr, familyName))
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * Find the closest matching typeface to the specified familyName and style
     * and return a ref to it. The caller must call [.close] on the returned
     * object. Will return null if no 'good' match is found.
     *
     * Passing null as the parameter for `familyName` will return the
     * default system font.
     *
     * It is possible that this will return a style set not accessible from
     * [.makeStyleSet] or [.matchFamily] due to hidden or
     * auto-activated fonts.
     */
    fun matchFamilyStyle(familyName: String?, style: FontStyle): Typeface? {
        return try {
            Stats.onNativeCall()
            val ptr = _nMatchFamilyStyle(ptr, familyName, style.value)
            if (ptr == 0L) null else Typeface(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun matchFamiliesStyle(families: Array<String?>, style: FontStyle): Typeface? {
        for (family in families) {
            val typeface = matchFamilyStyle(family, style)
            if (typeface != null) return typeface
        }
        return null
    }

    /**
     * Use the system fallback to find a typeface for the given character.
     * Note that bcp47 is a combination of ISO 639, 15924, and 3166-1 codes,
     * so it is fine to just pass a ISO 639 here.
     *
     * Will return null if no family can be found for the character
     * in the system fallback.
     *
     * Passing `null` as the parameter for `familyName` will return the
     * default system font.
     *
     * bcp47[0] is the least significant fallback, bcp47[bcp47.length-1] is the
     * most significant. If no specified bcp47 codes match, any font with the
     * requested character will be matched.
     */
    fun matchFamilyStyleCharacter(
        familyName: String?,
        style: FontStyle,
        bcp47: Array<String?>?,
        character: Int
    ): Typeface? {
        return try {
            Stats.onNativeCall()
            val ptr = _nMatchFamilyStyleCharacter(ptr, familyName, style.value, bcp47, character)
            if (ptr == 0L) null else Typeface(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun matchFamiliesStyleCharacter(
        families: Array<String?>,
        style: FontStyle,
        bcp47: Array<String?>?,
        character: Int
    ): Typeface? {
        for (family in families) {
            val typeface = matchFamilyStyleCharacter(family, style, bcp47, character)
            if (typeface != null) return typeface
        }
        return null
    }

    /**
     * Create a typeface for the specified data and TTC index (pass 0 for none)
     * or null if the data is not recognized. The caller must call [.close] on
     * the returned object if it is not null.
     */
    @JvmOverloads
    fun makeFromData(data: Data?, ttcIndex: Int = 0): Typeface? {
        return try {
            Stats.onNativeCall()
            val ptr =
                _nMakeFromData(ptr, getPtr(data), ttcIndex)
            if (ptr == 0L) null else Typeface(ptr)
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(data)
        }
    }

    object _DefaultHolder {
        /**
         * Return the default fontmgr.
         */
        val default = FontMgr(_nDefault(), false)

        init {
            Stats.onNativeCall()
        }
    }

    internal constructor(ptr: Long) : super(ptr)

    internal constructor(ptr: Long, allowClose: Boolean) : super(ptr, allowClose)
}