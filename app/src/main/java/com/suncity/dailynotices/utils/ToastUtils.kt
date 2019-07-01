package com.suncity.dailynotices.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Looper
import android.widget.Toast

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      ToastUtils
 * @Description:     作用描述
 * @UpdateDate:     1/6/2019
 */

object ToastUtils{

    private var context: Application? = null

    init {
        context = Config.getApplicationContext()
    }

    private var mToast: Toast? = null

    fun showShort(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            mToast?.setText(msg)
            mToast?.duration = Toast.LENGTH_SHORT
        }
        mToast?.show()
    }

    fun show(msg: String) {
        showShort(msg)
    }

    fun showLong(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        } else {
            mToast?.setText(msg)
        }
        mToast?.show()
    }

    fun showSafeToast(activity: Activity, text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showShort(text)
        } else {
            activity.runOnUiThread { showShort(text) }
        }
    }

    fun showSafeToast(context: Context, text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showShort(text)
        } else {
            if (context is Activity) {
                context.runOnUiThread { showShort(text) }
            }
        }
    }

    fun showSafeToast(context: Context, textRes: Int) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showShort(UIUtils.getString(textRes))
        } else {
            if (context is Activity) {
                context.runOnUiThread { showShort(UIUtils.getString(textRes)) }
            }
        }
    }
}