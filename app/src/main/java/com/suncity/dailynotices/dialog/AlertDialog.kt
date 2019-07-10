package com.suncity.dailynotices.dialog


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import com.suncity.dailynotices.R


class AlertDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var lLayoutBg: LinearLayout? = null
    private var txtTitle: TextView? = null
    private var txtMsg: TextView? = null
    private var btnNeg: Button? = null
    private var btnPos: Button? = null
    private var imgLine: ImageView? = null
    private val display: Display
    private var showTitle = false
    private var showMsg = false
    private var showPosBtn = false
    private var showNegBtn = false

    init {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    @SuppressLint("InflateParams")
    fun builder(): AlertDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
            R.layout.view_alertdialog, null
        )

        // 获取自定义Dialog布局中的控件
        lLayoutBg = view.findViewById<View>(R.id.lLayout_bg) as LinearLayout
        txtTitle = view.findViewById<View>(R.id.txt_title) as TextView
        txtTitle?.visibility = View.GONE
        txtMsg = view.findViewById<View>(R.id.txt_msg) as TextView
        txtMsg?.visibility = View.GONE
        btnNeg = view.findViewById<View>(R.id.btn_neg) as Button
        btnNeg?.visibility = View.GONE
        btnPos = view.findViewById<View>(R.id.btn_pos) as Button
        btnPos?.visibility = View.GONE
        imgLine = view.findViewById<View>(R.id.img_line) as ImageView
        imgLine?.visibility = View.GONE

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog?.setContentView(view)

        // 调整dialog背景大小
        lLayoutBg?.layoutParams = FrameLayout.LayoutParams(
            (display
                .width * 0.85).toInt(), LayoutParams.WRAP_CONTENT
        )

        return this
    }

    fun setTitle(title: String): AlertDialog {
        showTitle = true
        if ("" == title) {
            txtTitle?.text = "标题"
        } else {
            txtTitle?.text = title
        }
        return this
    }

    fun setMsg(msg: String): AlertDialog {
        showMsg = true
        if ("" == msg) {
            txtMsg?.text = "内容"
        } else {
            txtMsg?.text = msg
        }
        return this
    }

    fun setCancelable(cancel: Boolean): AlertDialog {
        dialog?.setCancelable(cancel)
        return this
    }

    fun setPositiveButton(
        text: String,
        listener: OnClickListener
    ): AlertDialog {
        showPosBtn = true
        if ("" == text) {
            btnPos?.text = "确定"
        } else {
            btnPos?.text = text
        }
        btnPos?.setOnClickListener { v ->
            listener.onClick(v)
            dialog?.dismiss()
        }
        return this
    }

    fun setNegativeButton(
        text: String,
        listener: OnClickListener
    ): AlertDialog {
        showNegBtn = true
        if ("" == text) {
            btnNeg?.text = "取消"
        } else {
            btnNeg?.text = text
        }
        btnNeg?.setOnClickListener { v ->
            listener.onClick(v)
            dialog?.dismiss()
        }
        return this
    }

    private fun setLayout() {
        if (!showTitle && !showMsg) {
            txtTitle?.text = "提示"
            txtTitle?.visibility = View.VISIBLE
        }

        if (showTitle) {
            txtTitle?.visibility = View.VISIBLE
        }

        if (showMsg) {
            txtMsg?.visibility = View.VISIBLE
        }

        if (!showPosBtn && !showNegBtn) {
            btnPos?.text = "确定"
            btnPos?.visibility = View.VISIBLE
            btnPos?.setBackgroundResource(R.drawable.alertdialog_single_selector)
            btnPos?.setOnClickListener { dialog?.dismiss() }
        }

        if (showPosBtn && showNegBtn) {
            btnPos?.visibility = View.VISIBLE
            btnPos?.setBackgroundResource(R.drawable.alertdialog_right_selector)
            btnNeg?.visibility = View.VISIBLE
            btnNeg?.setBackgroundResource(R.drawable.alertdialog_left_selector)
            imgLine?.visibility = View.VISIBLE
        }

        if (showPosBtn && !showNegBtn) {
            btnPos?.visibility = View.VISIBLE
            btnPos?.setBackgroundResource(R.drawable.alertdialog_single_selector)
        }

        if (!showPosBtn && showNegBtn) {
            btnNeg?.visibility = View.VISIBLE
            btnNeg?.setBackgroundResource(R.drawable.alertdialog_single_selector)
        }
    }

    fun show() {
        setLayout()
        dialog?.show()
    }
}
