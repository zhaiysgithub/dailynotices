package com.suncity.dailynotices.dialog

import android.app.Dialog

interface DialogLifeCycleListener {

    fun onCreate(alertDialog: Dialog)

    fun onShow(alertDialog: Dialog)

    fun onDismiss()

}
