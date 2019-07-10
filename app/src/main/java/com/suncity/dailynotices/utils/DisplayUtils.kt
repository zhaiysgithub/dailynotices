package com.suncity.dailynotices.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      DisplayUtils
 * @Description:     作用描述
 * @UpdateDate:     31/5/2019
 */

object DisplayUtils{

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(dpValue: Float): Int {
        val scale = UIUtils.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dp2px(dpValue: Float):Float{
        val scale = UIUtils.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(pxValue: Float): Float {
        val scale = UIUtils.resources.displayMetrics.density
        return pxValue / scale
    }

    /** sp转换成px  */
    fun sp2px(spValue: Float): Float {
        val fontScale = UIUtils.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f)
    }

    /** px转换成sp  */
    fun px2sp(pxValue: Float): Int {
        val fontScale = UIUtils.resources.displayMetrics.density
        return (pxValue / fontScale + 0.5f).toInt()
    }
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.height
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    fun getScreenRealHeight(context: Context): Int {
        var dpi = 0
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            dpi = displayMetrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dpi
    }

    fun hiddenInputMethod(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            if (context is Activity) {
                imm.hideSoftInputFromWindow(context.window.decorView.windowToken, 0)
            }
        }
    }

    fun showInputMethod(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            if (context is Activity) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
            }
        }

    }

    fun isShowKeyboard(context: Context, v: View): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return if (imm.hideSoftInputFromWindow(v.windowToken, 0)) {
            imm.showSoftInput(v, 0)
            true
            //软键盘已弹出
        } else {
            false
            //软键盘未弹出
        }
    }
}