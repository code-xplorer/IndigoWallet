package com.ismail.creatvt.indigowallet.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R

class TopRoundedRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    val topLeftOval = RectF()
    val topRightOval = RectF()
    val topLeftRoundedPath = Path()
    val topRightRoundedPath = Path()

    val a = context.obtainStyledAttributes(attrs, R.styleable.TopRoundedRecyclerView)

    val radius = a.getDimension(R.styleable.TopRoundedRecyclerView_android_radius, 0f)
    val color = a.getColor(R.styleable.TopRoundedRecyclerView_android_color, 0xff0990f9.toInt())
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = this@TopRoundedRecyclerView.color
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


        topRightOval.left = w.toFloat() - (radius * 2)
        topRightOval.top = 0f
        topRightOval.right = w.toFloat()
        topRightOval.bottom = radius * 2

        topRightRoundedPath.moveTo(w.toFloat() - radius, 0f)
        topRightRoundedPath.arcTo(topRightOval, 270f, 90f)
        topRightRoundedPath.lineTo(w.toFloat(), 0f)
        topRightRoundedPath.lineTo(w.toFloat() - radius, 0f)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        canvas?.drawPath(topLeftRoundedPath, paint)
        canvas?.drawPath(topRightRoundedPath, paint)
    }

}