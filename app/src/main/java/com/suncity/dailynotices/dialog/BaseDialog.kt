package com.suncity.dailynotices.dialog

import android.app.Dialog

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BaseDialog
 * @Description:     dialog 基类
 * @UpdateDate:     8/7/2019
 */

abstract class BaseDialog{

    companion object {

        var dialogList = arrayListOf<BaseDialog>()

        fun unloadAllDialog() {
            try {
                dialogList.forEach {
                    it.doDismiss()
                }
                dialogList = arrayListOf()
            } catch (e: Exception) {
            }

        }
    }

    private var dialogLifeCycleListener: DialogLifeCycleListener? = null
    private var onDismissListener: OnDismissListener? = null

    fun setDialogLifeCycleListener(listener: DialogLifeCycleListener) {
        dialogLifeCycleListener = listener
    }

    fun getDialogLifeCycleListener(): DialogLifeCycleListener {
        if (dialogLifeCycleListener == null) {

            dialogLifeCycleListener = object : DialogLifeCycleListener{
                override fun onCreate(alertDialog: Dialog) {
                }

                override fun onShow(alertDialog: Dialog) {
                }

                override fun onDismiss() {
                }

            }
        }
        return dialogLifeCycleListener!!
    }

    fun cleanDialogLifeCycleListener() {
        dialogLifeCycleListener = null
    }

    fun getOnDismissListener(): OnDismissListener? {
        if (onDismissListener == null)
            onDismissListener = object : OnDismissListener{
                override fun onDismiss() {
                }

            }
        return onDismissListener
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    abstract fun showDialog()

    abstract fun doDismiss()


}