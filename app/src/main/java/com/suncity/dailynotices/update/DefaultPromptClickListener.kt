package com.suncity.dailynotices.update

import android.content.DialogInterface

class DefaultPromptClickListener(private val mAgent: IUpdateAgent, private val mIsAutoDismiss: Boolean) :
    DialogInterface.OnClickListener {

    override fun onClick(dialog: DialogInterface, which: Int) {

        when (which) {
            DialogInterface.BUTTON_POSITIVE -> mAgent.update()
            DialogInterface.BUTTON_NEUTRAL -> mAgent.ignore()
            DialogInterface.BUTTON_NEGATIVE -> {
            }
        }
        if (mIsAutoDismiss) {
            dialog.dismiss()
        }
    }
}