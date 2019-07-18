package com.suncity.dailynotices.islib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class SquareRelativeLayout : RelativeLayout {

    constructor(
        context: Context, attrs: AttributeSet,
        defStyle: Int
    ) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureS = widthMeasureSpec
        var heightMeasureS = heightMeasureSpec
        setMeasuredDimension(
            View.getDefaultSize(0, widthMeasureS),
            View.getDefaultSize(0, heightMeasureS)
        )

        val childWidthSize = measuredWidth
        // 高度和宽度一样
        widthMeasureS = View.MeasureSpec.makeMeasureSpec(
            childWidthSize, View.MeasureSpec.EXACTLY
        )
        heightMeasureS = widthMeasureS
        super.onMeasure(widthMeasureS, heightMeasureS)
    }

}