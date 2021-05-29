package org.jetbrains.skija

data class FilterMipmap(
    internal val _filterMode: FilterMode,
    internal val _mipmapMode: MipmapMode = MipmapMode.NONE,
) : SamplingMode {
    override fun pack(): Long {
        return 0x7FFFFFFFFFFFFFFFL and (_filterMode.ordinal.toLong() shl 32 or _mipmapMode.ordinal.toLong())
    }
}