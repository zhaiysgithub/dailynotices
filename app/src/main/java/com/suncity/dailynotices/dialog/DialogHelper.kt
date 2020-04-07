package com.suncity.dailynotices.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class DialogHelper : DialogFragment() {

    private var alertDialog: AlertDialog? = null
    private var onDismissListener: OnDismissListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return alertDialog!!
    }

    fun setAlertDialog(alertDialog: AlertDialog, onDismissListener: OnDismissListener): DialogHelper {
        this.alertDialog = alertDialog
        this.onDismissListener = onDismissListener
        return this
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (onDismissListener != null) onDismissListener!!.onDismiss()
    }

    /*override fun show(manager: FragmentManager, tag: String) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (dialog == null) {
            showsDialog = false
        }
        super.onActivityCreated(savedInstanceState)
    }
}
