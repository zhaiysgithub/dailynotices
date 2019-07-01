package com.suncity.dailynotices.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.suncity.dailynotices.BaseApplication

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 处理UI相关的工具类
 */

object UIUtils {

    init {

    }

    val context: Context
        get() = Config.getApplicationContext()

    /**
     * 获取资源
     */
    val resources: Resources
        get() = context.resources

    /**
     * 获取文字
     */
    fun getString(resId: Int): String {
        return resources.getString(resId)
    }

    /**
     * 获取文字数组
     */
    fun getStringArray(resId: Int): Array<String> {
        return resources.getStringArray(resId)
    }

    fun getIntArray(resId: Int): IntArray {
        return resources.getIntArray(resId)
    }

    /**
     * 获取dimen
     */
    fun getDimens(resId: Int): Int {
        return resources.getDimensionPixelSize(resId)
    }

    /**
     * 获取drawable
     */
    @Suppress("DEPRECATION")
    fun getDrawable(resId: Int): Drawable {
        return resources.getDrawable(resId)
    }

    /**
     * 获取颜色
     */
    @Suppress("DEPRECATION")
    fun getColor(resId: Int): Int {
        return resources.getColor(resId)
    }


}
