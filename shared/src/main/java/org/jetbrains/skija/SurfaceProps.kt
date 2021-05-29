package org.jetbrains.skija

import lombok.With

@With
data class SurfaceProps @JvmOverloads constructor(
    internal val deviceIndependentFonts: Boolean = false,
    internal val pixelGeometry: PixelGeometry = PixelGeometry.UNKNOWN,
) {

    internal fun getFlags(): Int {
        return 0 or if (deviceIndependentFonts) 1 else 0
    }

    internal fun getPixelGeometryOrdinal(): Int {
        return pixelGeometry.ordinal
    }
}