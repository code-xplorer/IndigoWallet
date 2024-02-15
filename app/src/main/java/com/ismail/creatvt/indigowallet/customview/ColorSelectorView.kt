package com.ismail.creatvt.indigowallet.customview

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.utility.indexOfOrNull

@BindingMethods(
    BindingMethod(
        attribute = "onColorSelected",
        method = "setOnColorSelectedListener",
        type = ColorSelectorView::class
    ),
    BindingMethod(
        attribute = "initialColor",
        method = "setInitialColor",
        type = ColorSelectorView::class
    )
)
class ColorSelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var a = context.obtainStyledAttributes(attrs, R.styleable.ColorSelectorView)

    private var trackHeight: Float = a.getDimension(
        R.styleable.ColorSelectorView_trackHeight,
        context.resources.getDimension(R.dimen.color_picker_track_height)
    )
    private var thumbRadius: Float = a.getDimension(
        R.styleable.ColorSelectorView_thumbRadius,
        context.resources.getDimension(R.dimen.color_picker_thumb_radius)
    )
    private var thumbColorInset: Float = a.getDimension(
        R.styleable.ColorSelectorView_thumbColorInset,
        context.resources.getDimension(R.dimen.color_picker_thumb_color_inset)
    )

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

    private var track = RectF()
    private var trackClipPath = Path()

    private var colors = context.resources.getIntArray(
        a.getResourceId(
            R.styleable.ColorSelectorView_colors,
            R.array.color_selector_default_colors
        )
    )
    var defaultColorWidth =
        context.resources.getDimension(R.dimen.color_picker_default_track_color_width)

    var trackColorRect = RectF()
    var trackColorWidth = 10f
    var trackColorPaint = Paint(ANTI_ALIAS_FLAG)

    var thumbRect = RectF()
    var thumbInsetRect = RectF()

    var thumbPaint = Paint(ANTI_ALIAS_FLAG)
    var thumbColorPaint = Paint(ANTI_ALIAS_FLAG)

    var selectedColor = a.getColor(R.styleable.ColorSelectorView_selectedColor, colors[4])
    var selectedPosition = a.getInt(
        R.styleable.ColorSelectorView_selectedPosition,
        colors.indexOfOrNull(selectedColor) ?: 0
    )
        set(value) {
            field = value
            createHeptics()
            postInvalidate()
            selectedColor = colors[value]
            selectedListener?.onColorSelected(this, selectedPosition, selectedColor)
        }

    init {
        track.left = thumbRadius
        trackColorRect.left = thumbRadius

        setPadding((thumbRadius / 4).toInt())

        thumbPaint.setShadowLayer(thumbRadius / 4, 0f, 0f, Color.parseColor("#ff888888"))
    }

    fun setInitialColor(colorRes: Int) {
        val color = ResourcesCompat.getColor(resources, colorRes, context.theme)
        val index = colors.indexOf(color)
        if (index >= 0) {
            selectedPosition = index
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val availableWidth = w - paddingStart - paddingEnd

        trackColorWidth = (availableWidth - (thumbRadius * 2)) / (colors.size - 1)

        track.left = paddingStart + thumbRadius - trackColorWidth / 2
        track.right = paddingStart + availableWidth - thumbRadius + trackColorWidth / 2
        track.top = h / 2 - trackHeight / 2
        track.bottom = track.top + trackHeight

        trackColorRect.top = track.top
        trackColorRect.bottom = track.bottom

        thumbRect.top = track.centerY() - thumbRadius
        thumbRect.bottom = track.centerY() + thumbRadius

        thumbInsetRect.set(thumbRect)
        thumbInsetRect.inset(thumbColorInset, thumbColorInset)

        trackClipPath.addRoundRect(track, trackHeight / 2f, trackHeight / 2f, Path.Direction.CW)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: return super.onTouchEvent(event)
        if (x > track.right || x < track.left) {
            return false
        }
        val position = ((x - track.left) / trackColorWidth).toInt()
        if (event.action == MotionEvent.ACTION_DOWN
            || event.action == MotionEvent.ACTION_POINTER_DOWN
            || event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_POINTER_UP
            || event.action == MotionEvent.ACTION_MOVE
        ) {
            if (position != selectedPosition) {
                selectedPosition = position
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun createHeptics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator?.vibrate(effect)
        } else {
            vibrator?.vibrate(5)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        canvas?.clipPath(trackClipPath)
        for (colorIndex in colors.indices) {
            trackColorPaint.color = colors[colorIndex]
            trackColorRect.left =
                track.left + (colorIndex * trackColorWidth)
            trackColorRect.right = trackColorRect.left + trackColorWidth
            canvas?.drawRect(trackColorRect, trackColorPaint)
        }
        canvas?.restore()

        thumbRect.left = paddingStart + (selectedPosition * trackColorWidth)
        thumbRect.right = thumbRect.left + (thumbRadius * 2f)
        thumbPaint.color = Color.WHITE

        thumbInsetRect.left = thumbRect.left + thumbColorInset
        thumbInsetRect.right = thumbRect.right - thumbColorInset
        thumbColorPaint.color = selectedColor

        canvas?.drawRoundRect(thumbRect, thumbRadius, thumbRadius, thumbPaint)
        canvas?.drawRoundRect(thumbInsetRect, thumbRadius, thumbRadius, thumbColorPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth =
            (paddingStart + paddingEnd + (defaultColorWidth * colors.size) + thumbRadius * 2 - defaultColorWidth).toInt()
        val desiredHeight = (thumbRadius * 2 + paddingTop + paddingBottom).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width: Int = if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            Math.min(desiredWidth, widthSize)
        } else {
            //Be whatever you want
            desiredWidth
        }

        //Measure Height
        val height: Int = if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            Math.min(desiredHeight, heightSize)
        } else {
            //Be whatever you want
            desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    private var selectedListener: OnColorSelectedListener? = null

    fun setOnColorSelectedListener(listener: OnColorSelectedListener) {
        selectedListener = listener
    }

    interface OnColorSelectedListener {
        fun onColorSelected(
            colorSelectorView: ColorSelectorView,
            position: Int,
            colorValue: Int
        )
    }
}