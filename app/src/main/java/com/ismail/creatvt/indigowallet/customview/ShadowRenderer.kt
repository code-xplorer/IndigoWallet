package com.ismail.creatvt.indigowallet.customview

import android.graphics.*
import androidx.core.graphics.ColorUtils

class ShadowRenderer @JvmOverloads constructor(color: Int = Color.BLACK) {
    val shadowPaint: Paint
    private val cornerShadowPaint: Paint
    private val edgeShadowPaint: Paint
    private var shadowStartColor = 0
    private var shadowMiddleColor = 0
    private var shadowEndColor = 0
    private val scratch = Path()
    private val transparentPaint = Paint()
    fun setShadowColor(color: Int) {
        shadowStartColor = ColorUtils.setAlphaComponent(color, COLOR_ALPHA_START)
        shadowMiddleColor = ColorUtils.setAlphaComponent(color, COLOR_ALPHA_MIDDLE)
        shadowEndColor = ColorUtils.setAlphaComponent(color, COLOR_ALPHA_END)
        shadowPaint.color = shadowStartColor
    }

    /** Draws an edge shadow on the canvas in the current bounds with the matrix transform applied.  */
    fun drawEdgeShadow(
        canvas: Canvas, transform: Matrix?, bounds: RectF, elevation: Int
    ) {
        bounds.bottom += elevation.toFloat()
        bounds.offset(0f, -elevation.toFloat())
        edgeColors[0] = shadowEndColor
        edgeColors[1] = shadowMiddleColor
        edgeColors[2] = shadowStartColor
        edgeShadowPaint.shader = LinearGradient(
            bounds.left,
            bounds.top,
            bounds.left,
            bounds.bottom,
            edgeColors,
            edgePositions,
            Shader.TileMode.CLAMP
        )
        canvas.save()
        canvas.concat(transform)
        canvas.drawRect(bounds, edgeShadowPaint)
        canvas.restore()
    }

    /**
     * Draws a corner shadow on the canvas in the current bounds with the matrix transform applied.
     */
    fun drawCornerShadow(
        canvas: Canvas,
        matrix: Matrix?,
        bounds: RectF,
        elevation: Int,
        startAngle: Float,
        sweepAngle: Float
    ) {
        val drawShadowInsideBounds = sweepAngle < 0
        val arcBounds = scratch
        if (drawShadowInsideBounds) {
            cornerColors[0] = 0
            cornerColors[1] = shadowEndColor
            cornerColors[2] = shadowMiddleColor
            cornerColors[3] = shadowStartColor
        } else {
            // Calculate the arc bounds to prevent drawing shadow in the same part of the arc.
            arcBounds.rewind()
            arcBounds.moveTo(bounds.centerX(), bounds.centerY())
            arcBounds.arcTo(bounds, startAngle, sweepAngle)
            arcBounds.close()
            bounds.inset(-elevation.toFloat(), -elevation.toFloat())
            cornerColors[0] = 0
            cornerColors[1] = shadowStartColor
            cornerColors[2] = shadowMiddleColor
            cornerColors[3] = shadowEndColor
        }
        val radius = bounds.width() / 2f
        // The shadow is not big enough to draw.
        if (radius <= 0) {
            return
        }
        val startRatio = 1f - elevation / radius
        val midRatio = startRatio + (1f - startRatio) / 2f
        cornerPositions[1] = startRatio
        cornerPositions[2] = midRatio
        cornerShadowPaint.shader = RadialGradient(
            bounds.centerX(),
            bounds.centerY(),
            radius,
            cornerColors,
            cornerPositions,
            Shader.TileMode.CLAMP
        )

        canvas.save()
        canvas.concat(matrix)
        if (!drawShadowInsideBounds) {
            canvas.clipPath(arcBounds)
            // This line is required for the next drawArc to work correctly, I think.
            canvas.drawPath(arcBounds, transparentPaint)
        }
        canvas.drawArc(bounds, startAngle, sweepAngle, true, cornerShadowPaint)
        canvas.restore()
    }

    companion object {
        /** Gradient start color of 68 which evaluates to approximately 26% opacity.  */
        private const val COLOR_ALPHA_START = 0x44

        /** Gradient start color of 20 which evaluates to approximately 8% opacity.  */
        private const val COLOR_ALPHA_MIDDLE = 0x14
        private const val COLOR_ALPHA_END = 0
        private val edgeColors = IntArray(3)

        /** Start, middle of shadow, and end of shadow positions  */
        private val edgePositions = floatArrayOf(0f, .5f, 1f)
        private val cornerColors = IntArray(4)

        /** Start, beginning of corner, middle of shadow, and end of shadow positions  */
        private val cornerPositions = floatArrayOf(0f, 0f, .5f, 1f)
    }

    init {
        shadowPaint = Paint()
        setShadowColor(color)
        transparentPaint.color = Color.TRANSPARENT
        cornerShadowPaint = Paint(Paint.DITHER_FLAG)
        cornerShadowPaint.style = Paint.Style.FILL
        edgeShadowPaint = Paint(cornerShadowPaint)
    }
}