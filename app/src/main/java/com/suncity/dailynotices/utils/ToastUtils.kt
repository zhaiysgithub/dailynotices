package com.suncity.dailynotices.utils

import android.annotation.SuppressLint
import android.app.Activity
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

    private var toast: Toast? = null

    @SuppressLint("ShowToast")
    fun showToast(text: String) {
        if (toast == null) {
            toast = Toast.makeText(Config.getApplicationContext(), text, Toast.LENGTH_SHORT)
        } else {
            toast?.setText(text)
        }
        toast?.show()
    }


    fun showSafeToast(activity: Activity, text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showToast(text)
        } else {
            activity.runOnUiThread { showToast(text) }
        }
    }


    fun showSafeToast(context: Context, text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showToast(text)
        } else {
            if (context is Activity){
                context.runOnUiThread { showToast(text) }
            }
        }
    }


}