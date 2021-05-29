package org.jetbrains.skija

import lombok.Data
import org.jetbrains.annotations.Contract
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * Matrix holds a 3x3 matrix for transforming coordinates. This allows mapping
 * Point and vectors with translation, scaling, skewing, rotation, and
 * perspective.
 *
 *
 * Matrix includes a hidden variable that classifies the type of matrix to
 * improve performance. Matrix is not thread safe unless getType() is called first.
 *
 * @see [https://fiddle.skia.org/c/@Matrix_063](https://fiddle.skia.org/c/@Matrix_063)
 */
@Data
class Matrix33(
    /**
     *
     * Matrix33 elements are in row-major order.
     *
     * <pre>`
     * | scaleX   skewX  transX |
     * |  skewY  scaleY  transY |
     * | persp0  persp1  persp2 |
    `</pre> *
     */
    internal vararg val mat: Float
) {

    init {
        require(mat.size == 9) { "Expected 9 elements, got ${this.mat.size}" }
    }

    fun makePreScale(sx: Float, sy: Float): Matrix33 {
        return Matrix33(
            this.mat[0] * sx, this.mat[1] * sy, this.mat[2],
            this.mat[3] * sx, this.mat[4] * sy, this.mat[5],
            this.mat[6] * sx, this.mat[7] * sy, this.mat[8]
        )
    }

    /**
     *
     * Creates Matrix33 by multiplying this by other. This can be thought of mapping by other before applying Matrix.
     *
     *
     * Given:
     *
     * <pre>`
     * | A B C |          | J K L |
     * this = | D E F |, other = | M N O |
     * | G H I |          | P Q R |
    `</pre> *
     *
     *
     * Returns:
     *
     * <pre>`
     * | A B C |   | J K L |   | AJ+BM+CP AK+BN+CQ AL+BO+CR |
     * this * other = | D E F | * | M N O | = | DJ+EM+FP DK+EN+FQ DL+EO+FR |
     * | G H I |   | P Q R |   | GJ+HM+IP GK+HN+IQ GL+HO+IR |
    `</pre> *
     *
     * @param other  Matrix on right side of multiply expression
     * @return       this multiplied by other
     */
    fun makeConcat(other: Matrix33): Matrix33 {
        return Matrix33(
            this.mat[0] * other.mat[0] + this.mat[1] * other.mat[3] + this.mat[2] * other.mat[6],
            this.mat[0] * other.mat[1] + this.mat[1] * other.mat[4] + this.mat[2] * other.mat[7],
            this.mat[0] * other.mat[2] + this.mat[1] * other.mat[5] + this.mat[2] * other.mat[8],
            this.mat[3] * other.mat[0] + this.mat[4] * other.mat[3] + this.mat[5] * other.mat[6],
            this.mat[3] * other.mat[1] + this.mat[4] * other.mat[4] + this.mat[5] * other.mat[7],
            this.mat[3] * other.mat[2] + this.mat[4] * other.mat[5] + this.mat[5] * other.mat[8],
            this.mat[6] * other.mat[0] + this.mat[7] * other.mat[3] + this.mat[8] * other.mat[6],
            this.mat[6] * other.mat[1] + this.mat[7] * other.mat[4] + this.mat[8] * other.mat[7],
            this.mat[6] * other.mat[2] + this.mat[7] * other.mat[5] + this.mat[8] * other.mat[8],
        )
    }

    /**
     *
     * When converting from Matrix33 to Matrix44, the third row and
     * column remain as identity:
     *
     * <pre>`
     * [ a b c ]      [ a b 0 c ]
     * [ d e f ]  ->  [ d e 0 f ]
     * [ g h i ]      [ 0 0 1 0 ]
     * [ g h 0 i ]
    `</pre> *
     */
    fun asMatrix44(): Matrix44 {
        return Matrix44(
            this.mat[0], this.mat[1], 0f, this.mat[2],
            this.mat[3], this.mat[4], 0f, this.mat[5],
            0f, 0f, 1f, 0f,
            this.mat[6], this.mat[7], 0f, this.mat[8]
        )
    }

    companion object {
        /**
         * An identity Matrix33:
         *
         * <pre>`
         * | 1 0 0 |
         * | 0 1 0 |
         * | 0 0 1 |
        `</pre> *
         */
        val IDENTITY = makeTranslate(0f, 0f)

        /**
         *
         * Creates a Matrix33 to translate by (dx, dy). Returned matrix is:
         *
         * <pre>`
         * | 1 0 dx |
         * | 0 1 dy |
         * | 0 0  1 |
        `</pre> *
         *
         * @param dx  horizontal translation
         * @param dy  vertical translation
         * @return    Matrix33 with translation
         */
        @Contract("_, _ -> new")
        fun makeTranslate(dx: Float, dy: Float): Matrix33 {
            return Matrix33(
                1f, 0f, dx,
                0f, 1f, dy,
                0f, 0f, 1f,
            )
        }

        /**
         *
         * Creates a Matrix33 to scale by s. Returned matrix is:
         *
         * <pre>`
         * | s 0 0 |
         * | 0 s 0 |
         * | 0 0 1 |
        `</pre> *
         *
         * @param s  scale factor
         * @return   Matrix33 with scale
         */
        fun makeScale(s: Float): Matrix33 {
            return makeScale(s, s)
        }

        /**
         *
         * Creates a Matrix33 to scale by (sx, sy). Returned matrix is:
         *
         * <pre> `
         * | sx  0  0 |
         * |  0 sy  0 |
         * |  0  0  1 |
        `</pre> *
         *
         * @param sx horizontal scale factor
         * @param sy vertical scale factor
         * @return   Matrix33 with scale
         */
        fun makeScale(sx: Float, sy: Float): Matrix33 {
            return Matrix33(
                sx, 0f, 0f,
                0f, sy, 0f,
                0f, 0f, 1f,
            )
        }

        /**
         * Creates a Matrix33 to rotate by |deg| about a pivot point at (0, 0).
         *
         * @param deg  rotation angle in degrees (positive rotates clockwise)
         * @return     Matrix33 with rotation
         */
        fun makeRotate(deg: Float): Matrix33 {
            val rad = Math.toRadians(deg.toDouble())
            var sin = sin(rad)
            var cos = cos(rad)
            val tolerance = (1f / (1 shl 12)).toDouble()
            if (abs(sin) <= tolerance) sin = 0.0
            if (abs(cos) <= tolerance) cos = 0.0
            return Matrix33(
                cos.toFloat(), (-sin).toFloat(), 0f,
                sin.toFloat(), cos.toFloat(), 0f,
                0f, 0f, 1f,
            )
        }

        /**
         * Creates a Matrix33 to rotate by |deg| about a pivot point at pivot.
         *
         * @param deg    rotation angle in degrees (positive rotates clockwise)
         * @param pivot  pivot point
         * @return       Matrix33 with rotation
         */
        fun makeRotate(deg: Float, pivot: Point): Matrix33 {
            return makeRotate(deg, pivot._x, pivot._y)
        }

        /**
         * Creates a Matrix33 to rotate by |deg| about a pivot point at (pivotx, pivoty).
         *
         * @param deg     rotation angle in degrees (positive rotates clockwise)
         * @param pivotx  x-coord of pivot
         * @param pivoty  y-coord of pivot
         * @return        Matrix33 with rotation
         */
        fun makeRotate(deg: Float, pivotx: Float, pivoty: Float): Matrix33 {
            val rad = Math.toRadians(deg.toDouble())
            var sin = sin(rad)
            var cos = cos(rad)
            val tolerance = (1f / (1 shl 12)).toDouble()
            if (abs(sin) <= tolerance) sin = 0.0
            if (abs(cos) <= tolerance) cos = 0.0
            return Matrix33(
                cos.toFloat(), (-sin).toFloat(), (pivotx - pivotx * cos + pivoty * sin).toFloat(),
                sin.toFloat(), cos.toFloat(), (pivoty - pivoty * cos - pivotx * sin).toFloat(),
                0f, 0f, 1f,
            )
        }

        /**
         *
         * Creates a Matrix33 to skew by (sx, sy). Returned matrix is:
         *
         * <pre> `
         * | 1  sx  0 |
         * | sy  1  0 |
         * |  0  0  1 |
        `</pre> *
         *
         * @param sx horizontal skew factor
         * @param sy vertical skew factor
         * @return   Matrix33 with skew
         */
        fun makeSkew(sx: Float, sy: Float): Matrix33 {
            return Matrix33(
                1f, sx, 0f,
                sy, 1f, 0f,
                0f, 0f, 1f,
            )
        }
    }
}