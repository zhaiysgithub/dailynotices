package com.suncity.dailynotices.utils

import android.app.Activity
import android.app.ProgressDialog

/**
 * 显示和隐藏progress.
 */

object ProgressUtil {

    @Suppress("DEPRECATION")
    private var mProgressDialog: ProgressDialog? = null

    val isShowing: Boolean
        get() = if (mProgressDialog == null) {
            false
        } else {
            mProgressDialog!!.isShowing
        }

    fun showProgress(activity: Activity) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(activity, "温馨提示", "正在提交中，请稍等")
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
