package com.suncity.dailynotices.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.suncity.dailynotices.R

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BottomDialogiOSSetting
 * @Description:     作用描述
 * @UpdateDate:     13/7/2019
 */
class BottomDialogiOSSetting(context: Context) : BottomDialogiOS(context) {


    private var clickCallback: ClickCallback? = null

    fun setClickCallback(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    interface ClickCallback {
        fun doLogout()
        fun doCancel()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_logout -> {
                if (clickCallback != null)
                    clickCallback?.doLogout()
            }

            R.id.tv_cancel -> {
                if (clickCallback != null)
                    clickCallback?.doCancel()
            }
        }
    }

    init {
        dialog?.setContentView(R.layout.dialog_setting_chioce)
        dialog?.findViewById<TextView>(R.id.tv_logout)?.setOnClickListener(this)
        dialog?.findViewById<TextView>(R.id.tv_cancel)?.setOnClickListener(this)
        setDialogLocation(context, dialog)
    }
}