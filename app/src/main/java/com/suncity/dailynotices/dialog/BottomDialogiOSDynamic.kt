package com.suncity.dailynotices.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.Config


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BottomChioceDialogiOS
 * @Description:     作用描述
 */
class BottomDialogiOSDynamic(context: Context, private val isMine: Boolean) : BottomDialogiOS(context) {


    private var clickCallback: ClickCallback? = null

    fun setClickCallback(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    interface ClickCallback {
        fun doShieldUserclick()
        fun doCancel()
        fun doReport()
        fun doComplaint()
        fun doDelPost()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_shield_user -> {
                if (isMine) {
                    clickCallback?.doDelPost()
                } else {
                    clickCallback?.doShieldUserclick()
                }
            }
            R.id.tv_report -> {
                clickCallback?.doReport()
            }
            R.id.tv_complain -> {
                clickCallback?.doComplaint()
            }

            R.id.tv_cancel -> {
                clickCallback?.doCancel()
            }
        }
    }




    private var tvShieldUser: TextView? = null
    private var tvReport: TextView? = null
    private var tvComplain: TextView? = null
    private var tvCancel: TextView? = null

    init {
        dialog?.setContentView(R.layout.dialog_home_dynamic_chioce)

        tvShieldUser = dialog?.findViewById(R.id.tv_shield_user)
        tvReport = dialog?.findViewById(R.id.tv_report)
        tvComplain = dialog?.findViewById(R.id.tv_complain)
        tvCancel = dialog?.findViewById(R.id.tv_cancel)

        if (isMine) {
            tvShieldUser?.text = Config.getString(R.string.str_del_post)
        } else {
            tvShieldUser?.text = Config.getString(R.string.str_shield_user)
        }

        tvReport?.visibility = if (isMine) View.GONE else View.VISIBLE
        tvComplain?.visibility = if (isMine) View.GONE else View.VISIBLE

        tvShieldUser?.setOnClickListener(this)
        tvReport?.setOnClickListener(this)
        tvComplain?.setOnClickListener(this)
        tvCancel?.setOnClickListener(this)
        setDialogLocation(context, dialog)
    }


}