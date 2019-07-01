package com.suncity.dailynotices.ui.views.tablayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.DisplayUtils


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.views.tablayout
 * @ClassName:      MsgView
 * @Description:    用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 */
class MsgView : TextView{

    private lateinit var mContext: Context

    constructor(context:Context):this(context, null)

    constructor(context:Context,attrs:AttributeSet?):this(context, attrs,0)

    constructor(context: Context,attrs: AttributeSet?,defStyle:Int):super(context, attrs,defStyle){
        mContext = context
        obtainAttributes(context, attrs)
    }
    var gdBackground = GradientDrawable()

    var bgColor: Int = 0
        set(value) {
            field = value
            setBgSelector()
        }

    var cornerRadius: Int = 0
        set(value) {
            field = DisplayUtils.dip2px(value.toFloat())
            setBgSelector()
        }

    var strokeWidth: Int = 0
        set(value) {
            field = DisplayUtils.dip2px(value.toFloat())
            setBgSelector()
        }

    var strokeColor: Int = 0
        set(value) {
            field = value
            setBgSelector()
        }

    var isRadiusHalfHeight: Boolean = false
        set(value) {
            field = value
            setBgSelector()
        }

    var isWidthHeightEqual: Boolean = false
        set(value) {
            field = value
            setBgSelector()
        }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MsgView)
        bgColor = ta.getColor(R.styleable.MsgView_mv_backgroundColor, Color.TRANSPARENT)
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MsgView_mv_cornerRadius, 0)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.MsgView_mv_strokeWidth, 0)
        strokeColor = ta.getColor(R.styleable.MsgView_mv_strokeColor, Color.TRANSPARENT)
        isRadiusHalfHeight = ta.getBoolean(R.styleable.MsgView_mv_isRadiusHalfHeight, false)
        isWidthHeightEqual = ta.getBoolean(R.styleable.MsgView_mv_isWidthHeightEqual, false)

        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isWidthHeightEqual && width > 0 && height > 0) {
            val max = Math.max(width, height)
            val measureSpec = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (isRadiusHalfHeight) {
            cornerRadius = height / 2
        } else {
            setBgSelector()
        }
    }

    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)
        gd.cornerRadius = cornerRadius.toFloat()
        gd.setStroke(strokeWidth, strokeColor)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setBgSelector() {
        val bg = StateListDrawable()

        setDrawable(gdBackground, bgColor, strokeColor)
        bg.addState(intArrayOf(-android.R.attr.state_pressed), gdBackground)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            background = bg
        } else {
            setBackgroundDrawable(bg)
        }
    }
}