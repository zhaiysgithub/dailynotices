package com.suncity.dailynotices.islib.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager : ViewPager {

    private var isLocked: Boolean = false

    constructor(context: Context) : super(context) {
        isLocked = false
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        isLocked = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isLocked) {
            return try {
                super.onInterceptTouchEvent(ev)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                false
            }

        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return !isLocked && super.onTouchEvent(event)
    }

    fun toggleLock() {
        isLocked = !isLocked
    }

}