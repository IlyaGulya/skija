package org.jetbrains.skija

import lombok.With

@With
data class SurfaceProps @JvmOverloads constructor(
    internal val _deviceIndependentFonts: Boolean = false,
    internal val _pixelGeometry: PixelGeometry = PixelGeometry.UNKNOWN,
) {

    internal fun _getFlags(): Int {
        return 0 or if (_deviceIndependentFonts) 1 else 0
    }

    internal fun _getPixelGeometryOrdinal(): Int {
        return _pixelGeometry.ordinal
    }
}