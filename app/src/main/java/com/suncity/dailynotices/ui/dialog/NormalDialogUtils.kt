package com.suncity.dailynotices.ui.dialog

import android.content.Context

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.dialog
 * @ClassName:      NormalDialogUtils
 * @Description:     作用描述
 * @UpdateDate:     24/7/2019
 */

object NormalDialogUtils {

    fun showNormalDialog(context: Context) {
        MProgressDialog.showProgress(context)
    }


    fun dismissNormalDialog() {
        MProgressDialog.dismissProgress()
    }

    fun normalDialogIsShowing(): Boolean {
        return MProgressDialog.isShowing()
    }


    fun showTextDialog(context: Context, text: String){
        MProgressDialog.showProgress(context,text)
    }

}