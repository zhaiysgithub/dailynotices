@file:Suppress("DEPRECATION")

package com.suncity.dailynotices.utils

import android.app.Activity
import android.app.ProgressDialog

/**
 * 显示和隐藏progress.
 */

object ProgressUtil {

    @Suppress("DEPRECATION")
    private var mProgressDialog: ProgressDialog? = null
    private val mTitle = "温馨提示"
    private val mMessage = "正在提交中，请稍等"

    val isShowing: Boolean
        get() = if (mProgressDialog == null) {
            false
        } else {
            mProgressDialog!!.isShowing
        }

    fun showProgress(activity: Activity,title:String? = mTitle,message:String? = mMessage) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(activity, title, message)
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        } else {
            mProgressDialog?.show()
        }

    }

    fun hideProgress() {
        if (mProgressDialog == null) {
            return
        } else {
            mProgressDialog?.dismiss()
            mProgressDialog = null
        }
    }
}
