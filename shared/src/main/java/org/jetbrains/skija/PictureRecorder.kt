package org.jetbrains.skija

import org.jetbrains.skija.impl.Library
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Stats
import java.lang.ref.Reference

class PictureRecorder internal constructor(
    ptr: Long
) : Managed(ptr, _FinalizerHolder.PTR) {
    companion object {
        // TODO
        /**
         *
         * Signal that the caller is done recording. This invalidates the canvas returned by
         * [.beginRecording]/[.getRecordingCanvas].
         *
         *
         * Unlike [.finishRecordingAsPicture], which returns an immutable picture,
         * the returned drawable may contain live references to other drawables (if they were added to
         * the recording canvas) and therefore this drawable will reflect the current state of those
         * nested drawables anytime it is drawn or a new picture is snapped from it (by calling
         * [Drawable.makePictureSnapshot]).
         */
        // public Drawable finishRecordingAsPicture(@NotNull Rect cull) {
        //     Stats.onNativeCall();
        //     return new Drawable(_nFinishRecordingAsDrawable(_ptr, 0));
        // }
        internal external fun _nMake(): Long
        internal external fun _nGetFinalizer(): Long
        internal external fun _nBeginRecording(ptr: Long, left: Float, top: Float, right: Float, bottom: Float): Long
        internal external fun _nGetRecordingCanvas(ptr: Long): Long
        internal external fun _nFinishRecordingAsPicture(ptr: Long): Long
        internal external fun _nFinishRecordingAsPictureWithCull(
            ptr: Long,
            left: Float,
            top: Float,
            right: Float,
            bottom: Float
        ): Long

        internal external fun _nFinishRecordingAsDrawable(ptr: Long): Long

        init {
            Library.staticLoad()
        }
    }

    constructor() : this(_nMake()) {
        Stats.onNativeCall()
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }

    /**
     * Returns the canvas that records the drawing commands.
     *
     * @param bounds the cull rect used when recording this picture. Any drawing the falls outside
     * of this rect is undefined, and may be drawn or it may not.
     * @return the canvas.
     */
    fun beginRecording(bounds: Rect): Canvas {
        return try {
            Stats.onNativeCall()
            Canvas(
                _nBeginRecording(
                    ptr,
                    bounds.left,
                    bounds.top,
                    bounds.right,
                    bounds.bottom
                ), false, this
            )
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * @return  the recording canvas if one is active, or null if recording is not active.
     */
    val recordingCanvas: Canvas?
        get() = try {
            Stats.onNativeCall()
            val ptr = _nGetRecordingCanvas(ptr)
            if (ptr == 0L) null else Canvas(ptr, false, this)
        } finally {
            Reference.reachabilityFence(this)
        }

    /**
     *
     * Signal that the caller is done recording. This invalidates the canvas returned by
     * [.beginRecording]/[.getRecordingCanvas].
     *
     *
     * The returned picture is immutable. If during recording drawables were added to the canvas,
     * these will have been "drawn" into a recording canvas, so that this resulting picture will
     * reflect their current state, but will not contain a live reference to the drawables
     * themselves.
     */
    fun finishRecordingAsPicture(): Picture {
        return try {
            Stats.onNativeCall()
            Picture(_nFinishRecordingAsPicture(ptr))
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    /**
     * Signal that the caller is done recording, and update the cull rect to use for bounding
     * box hierarchy (BBH) generation. The behavior is the same as calling
     * [.finishRecordingAsPicture], except that this method updates the cull rect
     * initially passed into [.beginRecording].
     *
     * @param cull the new culling rectangle to use as the overall bound for BBH generation
     * and subsequent culling operations.
     * @return the picture containing the recorded content.
     */
    fun finishRecordingAsPicture(cull: Rect): Picture {
        return try {
            Stats.onNativeCall()
            Picture(
                _nFinishRecordingAsPictureWithCull(
                    ptr,
                    cull.left,
                    cull.top,
                    cull.right,
                    cull.bottom
                )
            )
        } finally {
            Reference.reachabilityFence(this)
        }
    }
}