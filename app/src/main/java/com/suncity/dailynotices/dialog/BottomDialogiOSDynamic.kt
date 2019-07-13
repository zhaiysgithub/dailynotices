package com.suncity.dailynotices.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.suncity.dailynotices.R


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BottomChioceDialogiOS
 * @Description:     作用描述
 */
class BottomDialogiOSDynamic(context: Context) : BottomDialogiOS(context) {


    private var clickCallback: ClickCallback? = null

    fun setClickCallback(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    interface ClickCallback {
        fun doShieldUserclick()
        fun doCancel()
        fun doReport()
        fun doComplaint()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_shield_user -> {
                if (clickCallback != null)
                    clickCallback?.doShieldUserclick()
            }
            R.id.tv_report -> {
                if (clickCallback != null)
                    clickCallback?.doReport()
            }
            R.id.tv_complain -> {
                if (clickCallback != null)
                    clickCallback?.doComplaint()
            }

            R.id.tv_cancel -> {
                if (clickCallback != null)
                    clickCallback?.doCancel()
            }
        }
    }

    init {
        dialog?.setContentView(R.layout.dialog_home_dynamic_chioce)
        dialog?.findViewById<TextView>(R.id.tv_shield_user)?.setOnClickListener(this)
        dialog?.findViewById<TextView>(R.id.tv_report)?.setOnClickListener(this)
        dialog?.findViewById<TextView>(R.id.tv_complain)?.setOnClickListener(this)
        dialog?.findViewById<TextView>(R.id.tv_cancel)?.setOnClickListener(this)
        setDialogLocation(context, dialog)
    }
}