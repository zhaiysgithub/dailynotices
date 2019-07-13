package com.suncity.dailynotices.dialog

import android.view.ViewGroup
import android.view.WindowManager
import android.app.Dialog
import android.content.Context
import android.view.View
import com.suncity.dailynotices.R


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BottomDialogiOS
 * @Description:     fang iso 底部弹窗
 */
open class BottomDialogiOS(context: Context):View.OnClickListener {

    protected var dialog: Dialog? = null
    protected var mContext: Context = context

    init {
        this.dialog = Dialog(mContext, R.style.bottom_dialog_ios)
    }

    /**
     * 设置dialog从下方弹出
     * @param context
     * @param dialog
     */
    @Suppress("DEPRECATION")
    protected fun setDialogLocation(context: Context, dialog: Dialog?) {
        val window = dialog?.window
        window?.setWindowAnimations(R.style.main_menu_animstyle)
        val lp = window?.attributes
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp?.x = 0
        lp?.y = manager.defaultDisplay.height
        // 以下这两句是为了保证按钮可以水平满屏
        lp?.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置显示位置
        dialog?.onWindowAttributesChanged(lp)
        // 设置点击外围解散
        dialog?.setCanceledOnTouchOutside(true)
    }

    fun show() {
        if (dialog != null) {
            dialog!!.show()
        }
    }

    fun dismiss() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    override fun onClick(v: View) {
        dismiss()
    }
}