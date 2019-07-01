package com.suncity.dailynotices.ui.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.suncity.dailynotices.utils.DisplayUtils

/**
 * loadingView
 */
class LoadCircleView @JvmOverloads constructor(var mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(mContext, attrs, defStyleAttr) {

    private var mPadding = 0f
    private var mPaint: Paint? = null
    private var mWidth = 0
    private var currentLineIndex = 0

    init {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 8f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        mWidth = if (widthSpecMode != View.MeasureSpec.AT_MOST && heightSpecMode != View.MeasureSpec.AT_MOST) {
            if (widthSpecSize >= heightSpecSize) widthSpecSize else heightSpecSize
        } else if (widthSpecMode == View.MeasureSpec.AT_MOST && heightSpecMode != View.MeasureSpec.AT_MOST) {
            heightSpecSize
        } else if (widthSpecMode != View.MeasureSpec.AT_MOST) {
            widthSpecSize
        } else {
            DisplayUtils.dip2px(50f)
        }
        setMeasuredDimension(mWidth, mWidth)
        mPadding = 8f
    }

    override fun onDraw(canvas: Canvas) {
        // 圆心坐标 (center,center)
        val center = mWidth shr 1
        val radius = (mWidth shr 1) - 8
        if (currentLineIndex >= 12)
            currentLineIndex = 0
        // 画12根线
        for (i in 0..11) {
            if (i < currentLineIndex + 4 && i >= currentLineIndex) {
                mPaint!!.color = Color.GRAY
            } else if (currentLineIndex > 8 && i < currentLineIndex + 4 - 12) {
                mPaint!!.color = Color.GRAY
            } else {
                mPaint!!.color = Color.WHITE
            }

            canvas.drawLine(center.toFloat(), (center + 1.0 / 2 * radius).toFloat(),
                    center.toFloat(), (2 * radius).toFloat(), mPaint!!)
            canvas.rotate(30f, center.toFloat(), center.toFloat())
        }
        currentLineIndex++
        postInvalidateDelayed(50)
    }
}
