package com.suncity.dailynotices.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.suncity.dailynotices.R

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BottomDialogiOSCover
 * @Description:     作用描述
 * @UpdateDate:     25/7/2019
 */
class BottomDialogiOSItem(itemText:String, context: Context) : BottomDialogiOS(context) {

    private var clickCallback: ClickCallback? = null

    fun setClickCallback(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    interface ClickCallback {
        fun doItemClick()
        fun doItemCancel()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.dialog_bottom_ios_item -> {
                if (clickCallback != null)
                    clickCallback?.doItemClick()
            }

            R.id.dialog_bottom_ios_cancel -> {
                if (clickCallback != null)
                    clickCallback?.doItemCancel()
            }
        }
    }

    init {
        dialog?.setContentView(R.layout.dialog_bottom_ios)
        val item = dialog?.findViewById<TextView>(R.id.dialog_bottom_ios_item)
        val cancel = dialog?.findViewById<TextView>(R.id.dialog_bottom_ios_cancel)
        item?.text = itemText
        item?.setOnClickListener(this)
        cancel?.setOnClickListener(this)
        setDialogLocation(context, dialog)
    }
}