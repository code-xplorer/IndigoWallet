package com.ismail.creatvt.indigowallet.customview

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.widget.ScrollView
import com.ismail.creatvt.indigowallet.R

class TopRoundedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    val topLeftOval = RectF()
    val topRightOval = RectF()
    val topLeftRoundedPath = Path()
    val topRightRoundedPath = Path()
    val clipRect = RectF()
    val clipPath = Path()

    val a = context.obtainStyledAttributes(attrs, R.styleable.TopRoundedScrollView)

    val radius = a.getDimension(R.styleable.TopRoundedScrollView_android_radius, 0f)
    val color = a.getColor(R.styleable.TopRoundedScrollView_android_color, 0xff0990f9.toInt())
    val paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = this@TopRoundedScrollView.color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        topLeftOval.left = 0f
        topLeftOval.top = 0f
        topLeftOval.right = radius * 2
        topLeftOval.bottom = radius * 2

        topLeftRoundedPath.moveTo(0f, 0f)
        topLeftRoundedPath.lineTo(radius, 0f)
        topLeftRoundedPath.arcTo(topLeftOval, 270f, -90f)
        topLeftRoundedPath.lineTo(0f, 0f)

        clipRect.left = 0f
        clipRect.top = 0f
        clipRect.right = w.toFloat()
        clipRect.bottom = h.toFloat()

        // clearing any previously set clip path values
        clipPath.reset()

        //Adding clip rect to the clip path as a round rect
        //so that the children that are outside the radius are clipped
        clipPath.addRoundRect(clipRect, floatArrayOf(
            radius, radius, // top-left
            radius, radius, // top-right
            0f, 0f,  // bottom-left
            0f, 0f   // bottom-right
        ), Path.Direction.CW)

        topRightOval.left = w.toFloat() - (radius * 2)
        topRightOval.top = 0f
        topRightOval.right = w.toFloat()
        topRightOval.bottom = radius * 2

        topRightRoundedPath.moveTo(w.toFloat() - radius, 0f)
        topRightRoundedPath.arcTo(topRightOval, 270f, 90f)
        topRightRoundedPath.lineTo(w.toFloat(), 0f)
        topRightRoundedPath.lineTo(w.toFloat() - radius, 0f)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.clipPath(clipPath)
        super.onDraw(canvas)
        canvas?.restore()
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        canvas?.drawPath(topLeftRoundedPath, paint)
        canvas?.drawPath(topRightRoundedPath, paint)
    }
}