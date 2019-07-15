package com.suncity.dailynotices.ui.views.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class MultiTouchViewPager : ViewPager {

    private var mIsDisallowIntercept = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        mIsDisallowIntercept = disallowIntercept
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.pointerCount > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false)
            val handled = super.dispatchTouchEvent(ev)
            requestDisallowInterceptTouchEvent(true)
            handled
        } else {
            super.dispatchTouchEvent(ev)
        }
    }
}