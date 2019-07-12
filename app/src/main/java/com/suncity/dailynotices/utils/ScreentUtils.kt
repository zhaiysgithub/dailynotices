package com.suncity.dailynotices.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager
import com.suncity.dailynotices.R

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      ScreentUtils
 * @Description:     作用描述
 * @UpdateDate:     12/7/2019
 */

object ScreentUtils{


    fun isLand(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun getDisplayDimen(context: Context): Point {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun getStatusBarHeightPixel(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getActionBarHeightPixel(context: Context): Int {
        val tv = TypedValue()
        return when {
            context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true) -> TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
            context.theme.resolveAttribute(R.attr.actionBarSize, tv, true) -> TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
            else -> 0
        }
    }

    fun getTabHeight(context: Context): Int {
        return context.resources.getDimensionPixelSize(R.dimen.dp_48)
    }
}