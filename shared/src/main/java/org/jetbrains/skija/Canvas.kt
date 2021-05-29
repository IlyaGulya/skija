package org.jetbrains.skija

import org.jetbrains.annotations.Contract
import org.jetbrains.skija.Point.Companion.flatten
import org.jetbrains.skija.impl.Library.staticLoad
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats.onNativeCall
import java.lang.ref.Reference

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class Canvas internal constructor(
    ptr: Long,
    managed: Boolean,
    internal val owner: Any
) :
    Managed(ptr, FinalizerHolder.PTR, managed) {
    @Suppress("FunctionName", "MemberVisibilityCanBePrivate")
    companion object {
        external fun _nGetFinalizer(): Long
        external fun _nMakeFromBitmap(bitmapPtr: Long, flags: Int, pixelGeometry: Int): Long
        external fun _nDrawPoint(ptr: Long, x: Float, y: Float, paintPtr: Long)
        external fun _nDrawPoints(ptr: Long, mode: Int, coords: FloatArray?, paintPtr: Long)
        external fun _nDrawLine(ptr: Long, x0: Float, y0: Float, x1: Float, y1: Float, paintPtr: Long)
        external fun _nDrawArc(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            startAngle: Float,
            sweepAngle: Float,
            includeCenter: Boolean,
            paintPtr: Long
        )

        external fun _nDrawRect(ptr: Long, left: Float, top: Float, right: Float, bottom: Float, paintPtr: Long)
        external fun _nDrawOval(ptr: Long, left: Float, top: Float, right: Float, bottom: Float, paint: Long)
        external fun _nDrawRRect(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            radii: FloatArray?,
            paintPtr: Long
        )

        external fun _nDrawDRRect(
            ptr: Long,
            ol: Float,
            ot: Float,
            or: Float,
            ob: Float,
            oradii: FloatArray?,
            il: Float,
            it: Float,
            ir: Float,
            ib: Float,
            iradii: FloatArray?,
            paintPtr: Long
        )

        external fun _nDrawPath(ptr: Long, nativePath: Long, paintPtr: Long)
        external fun _nDrawImageRect(
            ptr: Long,
            nativeImage: Long,
            sl: Float,
            st: Float,
            sr: Float,
            sb: Float,
            dl: Float,
            dt: Float,
            dr: Float,
            db: Float,
            samplingMode: Long,
            paintPtr: Long,
            strict: Boolean
        )

        external fun _nDrawImageNine(
            ptr: Long,
            nativeImage: Long,
            cl: Int,
            ct: Int,
            cr: Int,
            cb: Int,
            dl: Float,
            dt: Float,
            dr: Float,
            db: Float,
            filterMode: Int,
            paintPtr: Long
        )

        external fun _nDrawRegion(ptr: Long, nativeRegion: Long, paintPtr: Long)
        external fun _nDrawString(ptr: Long, string: String?, x: Float, y: Float, font: Long, paint: Long)
        external fun _nDrawTextBlob(ptr: Long, blob: Long, x: Float, y: Float, paint: Long)
        external fun _nDrawPicture(ptr: Long, picturePtr: Long, matrix: FloatArray?, paintPtr: Long)
        external fun _nDrawVertices(
            ptr: Long,
            verticesMode: Int,
            cubics: FloatArray?,
            colors: IntArray?,
            texCoords: FloatArray?,
            indices: ShortArray?,
            blendMode: Int,
            paintPtr: Long
        )

        external fun _nDrawPatch(
            ptr: Long,
            cubics: FloatArray?,
            colors: IntArray?,
            texCoords: FloatArray?,
            blendMode: Int,
            paintPtr: Long
        )

        external fun _nDrawDrawable(ptr: Long, drawablePrt: Long, matrix: FloatArray?)
        external fun _nClear(ptr: Long, color: Int)
        external fun _nDrawPaint(ptr: Long, paintPtr: Long)
        external fun _nSetMatrix(ptr: Long, matrix: FloatArray?)
        external fun _nGetLocalToDevice(ptr: Long): FloatArray
        external fun _nResetMatrix(ptr: Long)
        external fun _nClipRect(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            mode: Int,
            antiAlias: Boolean
        )

        external fun _nClipRRect(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            radii: FloatArray?,
            mode: Int,
            antiAlias: Boolean
        )

        external fun _nClipPath(ptr: Long, nativePath: Long, mode: Int, antiAlias: Boolean)
        external fun _nClipRegion(ptr: Long, nativeRegion: Long, mode: Int)
        external fun _nConcat(ptr: Long, matrix: FloatArray?)
        external fun _nConcat44(ptr: Long, matrix: FloatArray?)
        external fun _nReadPixels(ptr: Long, bitmapPtr: Long, srcX: Int, srcY: Int): Boolean
        external fun _nWritePixels(ptr: Long, bitmapPtr: Long, x: Int, y: Int): Boolean
        external fun _nSave(ptr: Long): Int
        external fun _nSaveLayer(ptr: Long, paintPtr: Long): Int
        external fun _nSaveLayerRect(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            paintPtr: Long
        ): Int

        external fun _nGetSaveCount(ptr: Long): Int
        external fun _nRestore(ptr: Long)
        external fun _nRestoreToCount(ptr: Long, saveCount: Int)

        init {
            staticLoad()
        }
    }

    /**
     *
     * Constructs a canvas that draws into bitmap.
     * Use props to match the device characteristics, like LCD striping.
     *
     *
     * Bitmap is copied so that subsequently editing bitmap will not affect
     * constructed Canvas.
     *
     * @param bitmap  width, height, ColorType, ColorAlphaType, and pixel
     * storage of raster surface
     * @param surfaceProps   order and orientation of RGB striping; and whether to use
     * device independent fonts
     *
     * @see [https://fiddle.skia.org/c/@Canvas_const_SkBitmap_const_SkSurfaceProps](https://fiddle.skia.org/c/@Canvas_const_SkBitmap_const_SkSurfaceProps)
     */
    constructor(bitmap: Bitmap, surfaceProps: SurfaceProps) : this(
        _nMakeFromBitmap(
            bitmap.ptr,
            surfaceProps.getFlags(),
            surfaceProps.pixelGeometry.ordinal
        ), true, bitmap
    ) {
        onNativeCall()
        Reference.reachabilityFence(bitmap)
    }

    /**
     *
     * Constructs a canvas that draws into bitmap.
     * Sets default pixel geometry in constructed Surface.
     *
     *
     * Bitmap is copied so that subsequently editing bitmap will not affect
     * constructed Canvas.
     *
     *
     * May be deprecated in the future.
     *
     * @param bitmap  width, height, ColorType, ColorAlphaType, and pixel
     * storage of raster surface
     *
     * @see [https://fiddle.skia.org/c/@Canvas_copy_const_SkBitmap](https://fiddle.skia.org/c/@Canvas_copy_const_SkBitmap)
     */
    constructor(bitmap: Bitmap) : this(bitmap, SurfaceProps())

    @Contract("_, _, _ -> this")
    fun drawPoint(x: Float, y: Float, paint: Paint): Canvas {
        onNativeCall()
        _nDrawPoint(ptr, x, y, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * The shape of point drawn depends on paint
     * PaintStrokeCap. If paint is set to [PaintStrokeCap.ROUND], each point draws a
     * circle of diameter Paint stroke width. If paint is set to [PaintStrokeCap.SQUARE]
     * or [PaintStrokeCap.BUTT], each point draws a square of width and height
     * Paint stroke width.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawPoints(coords: Array<Point>, paint: Paint): Canvas {
        return drawPoints(coords.flatten(), paint)
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * The shape of point drawn depends on paint
     * PaintStrokeCap. If paint is set to [PaintStrokeCap.ROUND], each point draws a
     * circle of diameter Paint stroke width. If paint is set to [PaintStrokeCap.SQUARE]
     * or [PaintStrokeCap.BUTT], each point draws a square of width and height
     * Paint stroke width.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawPoints(coords: FloatArray, paint: Paint): Canvas {
        onNativeCall()
        _nDrawPoints(ptr, 0 /* SkCanvas::PointMode::kPoints_PointMode */, coords, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * Each pair of points draws a line segment.
     * One line is drawn for every two points; each point is used once. If count is odd,
     * the final point is ignored.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawLines(coords: Array<Point>, paint: Paint): Canvas {
        return drawLines(coords.flatten(), paint)
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * Each pair of points draws a line segment.
     * One line is drawn for every two points; each point is used once. If count is odd,
     * the final point is ignored.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawLines(coords: FloatArray, paint: Paint): Canvas {
        onNativeCall()
        _nDrawPoints(ptr, 1 /* SkCanvas::PointMode::kLines_PointMode */, coords, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * Each adjacent pair of points draws a line segment.
     * count minus one lines are drawn; the first and last point are used once.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawPolygon(coords: Array<Point>, paint: Paint): Canvas {
        return drawPolygon(coords.flatten(), paint)
    }

    /**
     *
     * Draws pts using clip, Matrix and Paint paint.
     *
     *
     * Each adjacent pair of points draws a line segment.
     * count minus one lines are drawn; the first and last point are used once.
     *
     *
     * Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to [PaintMode.STROKE].
     *
     *
     * Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawPoints](https://fiddle.skia.org/c/@Canvas_drawPoints)
     */
    @Contract("_, _ -> this")
    fun drawPolygon(coords: FloatArray, paint: Paint): Canvas {
        onNativeCall()
        _nDrawPoints(ptr, 2 /* SkCanvas::PointMode::kPolygon_PointMode */, coords, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _, _ -> this")
    fun drawLine(x0: Float, y0: Float, x1: Float, y1: Float, paint: Paint): Canvas {
        onNativeCall()
        _nDrawLine(ptr, x0, y0, x1, y1, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _, _, _, _, _ -> this")
    fun drawArc(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        startAngle: Float,
        sweepAngle: Float,
        includeCenter: Boolean,
        paint: Paint
    ): Canvas {
        onNativeCall()
        _nDrawArc(ptr, left, top, right, bottom, startAngle, sweepAngle, includeCenter, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _ -> this")
    fun drawRect(r: Rect, paint: Paint): Canvas {
        onNativeCall()
        _nDrawRect(ptr, r.left, r.top, r.right, r.bottom, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _ -> this")
    fun drawOval(r: Rect, paint: Paint): Canvas {
        onNativeCall()
        _nDrawOval(ptr, r.left, r.top, r.right, r.bottom, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _ -> this")
    fun drawCircle(x: Float, y: Float, radius: Float, paint: Paint): Canvas {
        onNativeCall()
        _nDrawOval(ptr, x - radius, y - radius, x + radius, y + radius, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _ -> this")
    fun drawRRect(r: RRect, paint: Paint): Canvas {
        onNativeCall()
        _nDrawRRect(ptr, r.left, r.top, r.right, r.bottom, r.radii, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _ -> this")
    fun drawDRRect(outer: RRect, inner: RRect, paint: Paint): Canvas {
        onNativeCall()
        _nDrawDRRect(
            ptr,
            outer.left,
            outer.top,
            outer.right,
            outer.bottom,
            outer.radii,
            inner.left,
            inner.top,
            inner.right,
            inner.bottom,
            inner.radii,
            paint.toPointer()
        )
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _ -> this")
    fun drawRectShadow(r: Rect, dx: Float, dy: Float, blur: Float, color: Int): Canvas {
        return drawRectShadow(r, dx, dy, blur, 0f, color)
    }

    @Contract("_, _ -> this")
    fun drawRectShadow(r: Rect, dx: Float, dy: Float, blur: Float, spread: Float, color: Int): Canvas {
        val insides = r.inflate(-1f)
        if (!insides.isEmpty) {
            save()
            if (insides is RRect) clipRRect(insides, ClipMode.DIFFERENCE) else clipRect(insides, ClipMode.DIFFERENCE)
            drawRectShadowNoclip(r, dx, dy, blur, spread, color)
            restore()
        } else drawRectShadowNoclip(r, dx, dy, blur, spread, color)
        return this
    }

    @Contract("_, _ -> this")
    fun drawRectShadowNoclip(r: Rect, dx: Float, dy: Float, blur: Float, spread: Float, color: Int): Canvas {
        val outline = r.inflate(spread)
        ImageFilter.makeDropShadowOnly(dx, dy, blur / 2f, blur / 2f, color).use { f ->
            Paint().use { p ->
                p.setImageFilter(f)
                if (outline is RRect) drawRRect(outline, p) else drawRect(outline, p)
            }
        }
        return this
    }

    @Contract("_, _ -> this")
    fun drawPath(path: Path, paint: Paint): Canvas {
        onNativeCall()
        _nDrawPath(ptr, path.toPointer(), paint.toPointer())
        Reference.reachabilityFence(path)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _ -> this")
    fun drawImage(image: Image, left: Float, top: Float): Canvas {
        return drawImageRect(
            image,
            Rect.makeWH(image.width.toFloat(), image.height.toFloat()),
            Rect.makeXYWH(left, top, image.width.toFloat(), image.height.toFloat()),
            SamplingMode.DEFAULT,
            null,
            true
        )
    }

    @Contract("_, _, _, _ -> this")
    fun drawImage(image: Image, left: Float, top: Float, paint: Paint?): Canvas {
        return drawImageRect(
            image,
            Rect.makeWH(image.width.toFloat(), image.height.toFloat()),
            Rect.makeXYWH(left, top, image.width.toFloat(), image.height.toFloat()),
            SamplingMode.DEFAULT,
            paint,
            true
        )
    }

    @Contract("_, _ -> this")
    fun drawImageRect(image: Image, dst: Rect): Canvas {
        return drawImageRect(
            image,
            Rect.makeWH(image.width.toFloat(), image.height.toFloat()),
            dst,
            SamplingMode.DEFAULT,
            null,
            true
        )
    }

    @Contract("_, _, _ -> this")
    fun drawImageRect(image: Image, dst: Rect, paint: Paint?): Canvas {
        return drawImageRect(
            image,
            Rect.makeWH(image.width.toFloat(), image.height.toFloat()),
            dst,
            SamplingMode.DEFAULT,
            paint,
            true
        )
    }

    @Contract("_, _, _, _ -> this")
    fun drawImageRect(image: Image, src: Rect, dst: Rect): Canvas {
        return drawImageRect(image, src, dst, SamplingMode.DEFAULT, null, true)
    }

    @Contract("_, _, _, _ -> this")
    fun drawImageRect(image: Image, src: Rect, dst: Rect, paint: Paint?): Canvas {
        return drawImageRect(image, src, dst, SamplingMode.DEFAULT, paint, true)
    }

    @Contract("_, _, _, _, _ -> this")
    fun drawImageRect(image: Image, src: Rect, dst: Rect, paint: Paint?, strict: Boolean): Canvas {
        return drawImageRect(image, src, dst, SamplingMode.DEFAULT, paint, strict)
    }

    @Contract("_, _, _, _, _ -> this")
    fun drawImageRect(
        image: Image,
        src: Rect,
        dst: Rect,
        samplingMode: SamplingMode,
        paint: Paint?,
        strict: Boolean
    ): Canvas {
        onNativeCall()
        _nDrawImageRect(
            ptr,
            image.toPointer(),
            src.left,
            src.top,
            src.right,
            src.bottom,
            dst.left,
            dst.top,
            dst.right,
            dst.bottom,
            samplingMode.pack(),
            paint.toPointer(),
            strict,
        )
        Reference.reachabilityFence(image)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _, _ -> this")
    fun drawImageNine(image: Image, center: IRect, dst: Rect, filterMode: FilterMode, paint: Paint?): Canvas {
        onNativeCall()
        _nDrawImageNine(
            ptr,
            image.toPointer(),
            center.left,
            center.top,
            center.right,
            center.bottom,
            dst.left,
            dst.top,
            dst.right,
            dst.bottom,
            filterMode.ordinal,
            paint.toPointer(),
        )
        Reference.reachabilityFence(image)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _ -> this")
    fun drawRegion(r: Region, paint: Paint): Canvas {
        onNativeCall()
        _nDrawRegion(ptr, r.toPointer(), paint.toPointer())
        Reference.reachabilityFence(r)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _, _ -> this")
    fun drawString(s: String, x: Float, y: Float, font: Font?, paint: Paint): Canvas {
        onNativeCall()
        _nDrawString(ptr, s, x, y, font.toPointer(), paint.toPointer())
        Reference.reachabilityFence(font)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _ -> this")
    fun drawTextBlob(blob: TextBlob, x: Float, y: Float, paint: Paint): Canvas {
        onNativeCall()
        _nDrawTextBlob(ptr, blob.toPointer(), x, y, paint.toPointer())
        Reference.reachabilityFence(blob)
        Reference.reachabilityFence(paint)
        return this
    }

    @Contract("_, _, _, _ -> this")
    fun drawTextLine(line: TextLine, x: Float, y: Float, paint: Paint): Canvas {
        line.textBlob.use { blob ->
            if (blob != null) {
                drawTextBlob(blob, x, y, paint)
            }
        }
        return this
    }

    @Contract("_ -> this")
    fun drawPicture(picture: Picture): Canvas {
        return drawPicture(picture, null, null)
    }

    @Contract("_, _, _ -> this")
    fun drawPicture(picture: Picture, matrix: Matrix33?, paint: Paint?): Canvas {
        onNativeCall()
        _nDrawPicture(ptr, picture.toPointer(), matrix?.mat, paint.toPointer())
        Reference.reachabilityFence(picture)
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws a triangle mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader, the shader is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _ -> this")
    fun drawTriangles(positions: Array<Point>, colors: IntArray?, paint: Paint): Canvas {
        return drawTriangles(positions, colors, null, null, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _ -> this")
    fun drawTriangles(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        paint: Paint
    ): Canvas {
        return drawTriangles(positions, colors, texCoords, indices, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _, _ -> this")
    fun drawTriangles(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        mode: BlendMode,
        paint: Paint
    ): Canvas {
        require(positions.size % 3 == 0) { "Expected positions.length % 3 == 0, got: ${positions.size}" }
        require(colors == null || colors.size == positions.size) { "Expected colors.length == positions.length, got: ${colors!!.size} != ${positions.size}" }
        require(texCoords == null || texCoords.size == positions.size) { "Expected texCoords.length == positions.length, got: ${texCoords!!.size} != ${positions.size}" }
        onNativeCall()
        _nDrawVertices(
            ptr,
            0, /* kTriangles_VertexMode */
            positions.flatten(),
            colors,
            texCoords.flatten(),
            indices,
            mode.ordinal,
            paint.toPointer(),
        )
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws a triangle strip mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader, the shader is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _ -> this")
    fun drawTriangleStrip(positions: Array<Point>, colors: IntArray?, paint: Paint): Canvas {
        return drawTriangleStrip(positions, colors, null, null, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle strip mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _ -> this")
    fun drawTriangleStrip(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        paint: Paint
    ): Canvas {
        return drawTriangleStrip(positions, colors, texCoords, indices, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle strip mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _, _ -> this")
    fun drawTriangleStrip(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        mode: BlendMode,
        paint: Paint
    ): Canvas {
        assert(colors == null || colors.size == positions.size) { "Expected colors.length == positions.length, got: ${colors?.size} != ${positions.size}" }
        assert(texCoords == null || texCoords.size == positions.size) { "Expected texCoords.length == positions.length, got: ${texCoords?.size} != ${positions.size}" }
        onNativeCall()
        _nDrawVertices(
            ptr,
            1, /* kTriangleStrip_VertexMode */
            positions.flatten(),
            colors,
            texCoords.flatten(),
            indices,
            mode.ordinal,
            paint.toPointer(),
        )
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws a triangle fan mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader, the shader is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _ -> this")
    fun drawTriangleFan(positions: Array<Point>, colors: IntArray?, paint: Paint): Canvas {
        return drawTriangleFan(positions, colors, null, null, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle fan mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _ -> this")
    fun drawTriangleFan(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        paint: Paint
    ): Canvas {
        return drawTriangleFan(positions, colors, texCoords, indices, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a triangle fan mesh, using clip and Matrix.
     *
     *
     * If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.
     *
     *
     * If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param indices    with which indices points should be drawn; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices](https://fiddle.skia.org/c/@Canvas_drawVertices)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawVertices_2](https://fiddle.skia.org/c/@Canvas_drawVertices_2)
     */
    @Contract("_, _, _, _, _, _ -> this")
    fun drawTriangleFan(
        positions: Array<Point>,
        colors: IntArray?,
        texCoords: Array<Point>?,
        indices: ShortArray?,
        mode: BlendMode,
        paint: Paint
    ): Canvas {
        assert(colors == null || colors.size == positions.size) { "Expected colors.length == positions.length, got: ${colors?.size} != ${positions.size}" }
        assert(texCoords == null || texCoords.size == positions.size) { "Expected texCoords.length == positions.length, got: ${texCoords?.size} != ${positions.size}" }
        onNativeCall()
        _nDrawVertices(
            ptr,
            2, /* kTriangleFan_VertexMode */
            positions.flatten(),
            colors,
            texCoords.flatten(),
            indices,
            mode.ordinal,
            paint.toPointer(),
        )
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.
     *
     *
     * Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture.
     *
     *
     * Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.
     *
     *
     * Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.
     *
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see [https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445](https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445)
     */
    @Contract("_, _, _ -> this")
    fun drawPatch(cubics: Array<Point>, colors: IntArray, paint: Paint): Canvas {
        return drawPatch(cubics, colors, null, paint)
    }

    /**
     *
     * Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.
     *
     *
     * Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture.
     *
     *
     * Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.
     *
     *
     * Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.
     *
     *
     * If paint contains Shader, Point array texCoords maps Shader as texture to
     * corners in top-left, top-right, bottom-right, bottom-left order. If texCoords is
     * nullptr, Shader is mapped using positions (derived from cubics).
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners;
     * may be null
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see [https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445](https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445)
     */
    @Contract("_, _, _, _ -> this")
    fun drawPatch(cubics: Array<Point>, colors: IntArray, texCoords: Array<Point>?, paint: Paint): Canvas {
        return drawPatch(cubics, colors, texCoords, BlendMode.MODULATE, paint)
    }

    /**
     *
     * Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.
     *
     *
     * Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture; BlendMode mode combines color colors and Shader if
     * both are provided.
     *
     *
     * Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.
     *
     *
     * Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.
     *
     *
     * If paint contains Shader, Point array texCoords maps Shader as texture to
     * corners in top-left, top-right, bottom-right, bottom-left order. If texCoords is
     * nullptr, Shader is mapped using positions (derived from cubics).
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners;
     * may be null
     * @param mode       BlendMode for colors, and for Shader if paint has one
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see [https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445](https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445)
     */
    @Contract("_, _, _, _, _ -> this")
    fun drawPatch(
        cubics: Array<Point>,
        colors: IntArray,
        texCoords: Array<Point>?,
        mode: BlendMode,
        paint: Paint
    ): Canvas {
        assert(cubics.size == 12) { "Expected cubics.length == 12, got: ${cubics.size}" }
        assert(colors.size == 4) { "Expected colors.length == 4, got: ${colors.size}" }
        assert(texCoords == null || texCoords.size == 4) { "Expected texCoords.length == 4, got: " + texCoords!!.size }
        onNativeCall()
        _nDrawPatch(
            ptr,
            cubics.flatten(),
            colors,
            texCoords.flatten(),
            mode.ordinal,
            paint.toPointer()
        )
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     *
     * Draws Drawable drawable using clip and matrix.
     *
     *
     * If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @return          this
     */
    @Contract("_ -> this")
    fun drawDrawable(drawable: Drawable): Canvas {
        return drawDrawable(drawable, null)
    }

    /**
     *
     * Draws Drawable drawable using clip and matrix, offset by (x, y).
     *
     *
     * If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @param x         offset into Canvas writable pixels on x-axis
     * @param y         offset into Canvas writable pixels on y-axis
     * @return          this
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawDrawable_2](https://fiddle.skia.org/c/@Canvas_drawDrawable_2)
     */
    @Contract("_, _, _ -> this")
    fun drawDrawable(drawable: Drawable, x: Float, y: Float): Canvas {
        return drawDrawable(drawable, Matrix33.makeTranslate(x, y))
    }

    /**
     *
     * Draws Drawable drawable using clip and matrix, concatenated with
     * optional matrix.
     *
     *
     * If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @param matrix    transformation applied to drawing; may be null
     * @return          this
     *
     * @see [https://fiddle.skia.org/c/@Canvas_drawDrawable](https://fiddle.skia.org/c/@Canvas_drawDrawable)
     */
    @Contract("_, _ -> this")
    fun drawDrawable(drawable: Drawable, matrix: Matrix33?): Canvas {
        onNativeCall()
        _nDrawDrawable(ptr, drawable.toPointer(), matrix?.mat)
        Reference.reachabilityFence(drawable)
        return this
    }

    @Contract("_ -> this")
    fun clear(color: Int): Canvas {
        onNativeCall()
        _nClear(ptr, color)
        return this
    }

    @Contract("_ -> this")
    fun drawPaint(paint: Paint): Canvas {
        onNativeCall()
        _nDrawPaint(ptr, paint.toPointer())
        Reference.reachabilityFence(paint)
        return this
    }

    /**
     * Replaces Matrix with matrix.
     * Unlike concat(), any prior matrix state is overwritten.
     *
     * @param matrix  matrix to copy, replacing existing Matrix
     *
     * @see [https://fiddle.skia.org/c/@Canvas_setMatrix](https://fiddle.skia.org/c/@Canvas_setMatrix)
     */
    @Contract("_ -> this")
    fun setMatrix(matrix: Matrix33): Canvas {
        onNativeCall()
        _nSetMatrix(ptr, matrix.mat)
        return this
    }

    /**
     * Sets SkMatrix to the identity matrix.
     * Any prior matrix state is overwritten.
     *
     * @see [https://fiddle.skia.org/c/@Canvas_resetMatrix](https://fiddle.skia.org/c/@Canvas_resetMatrix)
     */
    @Contract("_ -> this")
    fun resetMatrix(matrix: Matrix33): Canvas {
        onNativeCall()
        _nResetMatrix(ptr)
        return this
    }

    /**
     * Returns the total transformation matrix for the canvas.
     */
    @get:Contract("_ -> new")
    val localToDevice: Matrix44
        get() = try {
            onNativeCall()
            val mat = _nGetLocalToDevice(ptr)
            Matrix44(*mat)
        } finally {
            Reference.reachabilityFence(this)
        }

    @get:Contract("_ -> new")
    val localToDeviceAsMatrix33: Matrix33
        get() = localToDevice.asMatrix33()

    @Contract("_, _, _ -> this")
    fun clipRect(r: Rect, mode: ClipMode, antiAlias: Boolean): Canvas {
        onNativeCall()
        _nClipRect(ptr, r.left, r.top, r.right, r.bottom, mode.ordinal, antiAlias)
        return this
    }

    @Contract("_, _ -> this")
    fun clipRect(r: Rect, mode: ClipMode): Canvas {
        return clipRect(r, mode, false)
    }

    @Contract("_, _ -> this")
    fun clipRect(r: Rect, antiAlias: Boolean): Canvas {
        return clipRect(r, ClipMode.INTERSECT, antiAlias)
    }

    @Contract("_ -> this")
    fun clipRect(r: Rect): Canvas {
        return clipRect(r, ClipMode.INTERSECT, false)
    }

    @Contract("_, _, _ -> this")
    fun clipRRect(r: RRect, mode: ClipMode, antiAlias: Boolean): Canvas {
        onNativeCall()
        _nClipRRect(ptr, r.left, r.top, r.right, r.bottom, r.radii, mode.ordinal, antiAlias)
        return this
    }

    @Contract("_, _ -> this")
    fun clipRRect(r: RRect, mode: ClipMode): Canvas {
        return clipRRect(r, mode, false)
    }

    @Contract("_, _ -> this")
    fun clipRRect(r: RRect, antiAlias: Boolean): Canvas {
        return clipRRect(r, ClipMode.INTERSECT, antiAlias)
    }

    @Contract("_ -> this")
    fun clipRRect(r: RRect): Canvas {
        return clipRRect(r, ClipMode.INTERSECT, false)
    }

    @Contract("_, _, _ -> this")
    fun clipPath(p: Path, mode: ClipMode, antiAlias: Boolean): Canvas {
        onNativeCall()
        _nClipPath(ptr, p.toPointer(), mode.ordinal, antiAlias)
        Reference.reachabilityFence(p)
        return this
    }

    @Contract("_, _ -> this")
    fun clipPath(p: Path, mode: ClipMode): Canvas {
        return clipPath(p, mode, false)
    }

    @Contract("_, _ -> this")
    fun clipPath(p: Path, antiAlias: Boolean): Canvas {
        return clipPath(p, ClipMode.INTERSECT, antiAlias)
    }

    @Contract("_ -> this")
    fun clipPath(p: Path): Canvas {
        return clipPath(p, ClipMode.INTERSECT, false)
    }

    @Contract("_, _ -> this")
    fun clipRegion(r: Region, mode: ClipMode): Canvas {
        onNativeCall()
        _nClipRegion(ptr, r.toPointer(), mode.ordinal)
        Reference.reachabilityFence(r)
        return this
    }

    @Contract("_ -> this")
    fun clipRegion(r: Region): Canvas {
        return clipRegion(r, ClipMode.INTERSECT)
    }

    @Contract("_, _ -> this")
    fun translate(dx: Float, dy: Float): Canvas {
        return concat(Matrix33.makeTranslate(dx, dy))
    }

    @Contract("_, _ -> this")
    fun scale(sx: Float, sy: Float): Canvas {
        return concat(Matrix33.makeScale(sx, sy))
    }

    /**
     * @param deg  angle in degrees
     * @return     this
     */
    @Contract("_ -> this")
    fun rotate(deg: Float): Canvas {
        return concat(Matrix33.makeRotate(deg))
    }

    @Contract("_, _ -> this")
    fun skew(sx: Float, sy: Float): Canvas {
        return concat(Matrix33.makeSkew(sx, sy))
    }

    @Contract("_ -> this")
    fun concat(matrix: Matrix33): Canvas {
        onNativeCall()
        _nConcat(ptr, matrix.mat)
        return this
    }

    @Contract("_ -> this")
    fun concat(matrix: Matrix44): Canvas {
        onNativeCall()
        _nConcat44(ptr, matrix.mat)
        return this
    }

    /**
     *
     * Copies Rect of pixels from Canvas into bitmap. Matrix and clip are
     * ignored.
     *
     *
     * Source Rect corners are (srcX, srcY) and (imageInfo().width(), imageInfo().height()).
     * Destination Rect corners are (0, 0) and (bitmap.width(), bitmap.height()).
     * Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to bitmap.colorType() and bitmap.alphaType() if required.
     *
     *
     * Pixels are readable when BaseDevice is raster, or backed by a GPU.
     * Pixels are not readable when Canvas is returned by Document::beginPage,
     * returned by PictureRecorder::beginRecording, or Canvas is the base of a utility
     * class like DebugCanvas.
     *
     *
     * Caller must allocate pixel storage in bitmap if needed.
     *
     *
     * SkBitmap values are converted only if ColorType and AlphaType
     * do not match. Only pixels within both source and destination rectangles
     * are copied. Bitmap pixels outside Rect intersection are unchanged.
     *
     *
     * Pass negative values for srcX or srcY to offset pixels across or down bitmap.
     *
     *
     * Does not copy, and returns false if:
     *
     *  * Source and destination rectangles do not intersect.
     *  * SkCanvas pixels could not be converted to bitmap.colorType() or bitmap.alphaType().
     *  * SkCanvas pixels are not readable; for instance, Canvas is document-based.
     *  * bitmap pixels could not be allocated.
     *  * bitmap.rowBytes() is too small to contain one row of pixels.
     *
     *
     * @param bitmap  storage for pixels copied from Canvas
     * @param srcX    offset into readable pixels on x-axis; may be negative
     * @param srcY    offset into readable pixels on y-axis; may be negative
     * @return        true if pixels were copied
     *
     * @see [https://fiddle.skia.org/c/@Canvas_readPixels_3](https://fiddle.skia.org/c/@Canvas_readPixels_3)
     */
    fun readPixels(bitmap: Bitmap, srcX: Int, srcY: Int): Boolean {
        return try {
            onNativeCall()
            _nReadPixels(ptr, bitmap.toPointer(), srcX, srcY)
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(bitmap)
        }
    }

    /**
     *
     * Copies Rect from pixels to Canvas. Matrix and clip are ignored.
     * Source Rect corners are (0, 0) and (bitmap.width(), bitmap.height()).
     *
     *
     * Destination Rect corners are (x, y) and
     * (imageInfo().width(), imageInfo().height()).
     *
     *
     * Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to getImageInfo().getColorType() and getImageInfo().getAlphaType() if required.
     *
     *
     * Pixels are writable when BaseDevice is raster, or backed by a GPU.
     * Pixels are not writable when Canvas is returned by Document::beginPage,
     * returned by PictureRecorder::beginRecording, or Canvas is the base of a utility
     * class like DebugCanvas.
     *
     *
     * Pixel values are converted only if ColorType and AlphaType
     * do not match. Only pixels within both source and destination rectangles
     * are copied. Canvas pixels outside Rect intersection are unchanged.
     *
     *
     * Pass negative values for x or y to offset pixels to the left or
     * above Canvas pixels.
     *
     *
     * Does not copy, and returns false if:
     *
     *  * Source and destination rectangles do not intersect.
     *  * bitmap does not have allocated pixels.
     *  * bitmap pixels could not be converted to Canvas getImageInfo().getColorType() or
     * getImageInfo().getAlphaType().
     *  * Canvas pixels are not writable; for instance, Canvas is document based.
     *  * bitmap pixels are inaccessible; for instance, bitmap wraps a texture.
     *
     *
     * @param bitmap  contains pixels copied to Canvas
     * @param x       offset into Canvas writable pixels on x-axis; may be negative
     * @param y       offset into Canvas writable pixels on y-axis; may be negative
     * @return        true if pixels were written to Canvas
     *
     * @see [https://fiddle.skia.org/c/@Canvas_writePixels_2](https://fiddle.skia.org/c/@Canvas_writePixels_2)
     *
     * @see [https://fiddle.skia.org/c/@State_Stack_a](https://fiddle.skia.org/c/@State_Stack_a)
     *
     * @see [https://fiddle.skia.org/c/@State_Stack_b](https://fiddle.skia.org/c/@State_Stack_b)
     */
    fun writePixels(bitmap: Bitmap, x: Int, y: Int): Boolean {
        return try {
            onNativeCall()
            _nWritePixels(ptr, bitmap.toPointer(), x, y)
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(bitmap)
        }
    }

    fun save(): Int {
        return try {
            onNativeCall()
            _nSave(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    fun saveLayer(left: Float, top: Float, right: Float, bottom: Float, paint: Paint?): Int {
        return try {
            onNativeCall()
            _nSaveLayerRect(
                ptr,
                left,
                top,
                right,
                bottom,
                paint.toPointer(),
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(paint)
        }
    }

    /**
     *
     * Saves Matrix and clip, and allocates a Bitmap for subsequent drawing.
     * Calling restore() discards changes to Matrix and clip, and draws the Bitmap.
     *
     *
     * Matrix may be changed by translate(), scale(), rotate(), skew(), concat(),
     * setMatrix(), and resetMatrix(). Clip may be changed by clipRect(), clipRRect(),
     * clipPath(), clipRegion().
     *
     *
     * Rect bounds suggests but does not define the Bitmap size. To clip drawing to
     * a specific rectangle, use clipRect().
     *
     *
     * Optional Paint paint applies alpha, ColorFilter, ImageFilter, and
     * BlendMode when restore() is called.
     *
     *
     * Call restoreToCount() with returned value to restore this and subsequent saves.
     *
     * @param bounds  hint to limit the size of the layer
     * @param paint   graphics state for layer; may be null
     * @return        depth of saved stack
     *
     * @see [https://fiddle.skia.org/c/@Canvas_saveLayer](https://fiddle.skia.org/c/@Canvas_saveLayer)
     *
     * @see [https://fiddle.skia.org/c/@Canvas_saveLayer_4](https://fiddle.skia.org/c/@Canvas_saveLayer_4)
     */
    fun saveLayer(bounds: Rect?, paint: Paint?): Int {
        return try {
            onNativeCall()
            if (bounds == null) _nSaveLayer(
                ptr,
                paint.toPointer(),
            ) else _nSaveLayerRect(
                ptr,
                bounds.left,
                bounds.top,
                bounds.right,
                bounds.bottom,
                paint.toPointer()
            )
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(paint)
        }
    }

    val saveCount: Int
        get() = try {
            onNativeCall()
            _nGetSaveCount(ptr)
        } finally {
            Reference.reachabilityFence(this)
        }

    @Contract("-> this")
    fun restore(): Canvas {
        onNativeCall()
        _nRestore(ptr)
        return this
    }

    @Contract("_ -> this")
    fun restoreToCount(saveCount: Int): Canvas {
        onNativeCall()
        _nRestoreToCount(ptr, saveCount)
        return this
    }

    internal object FinalizerHolder {
        val PTR = _nGetFinalizer()
    }
}