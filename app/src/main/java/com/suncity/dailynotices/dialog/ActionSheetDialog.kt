package com.suncity.dailynotices.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ScrollView
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.Config

class ActionSheetDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var txtTitle: TextView? = null
    private var txtCancel: TextView? = null
    private var lLayoutContent: LinearLayout? = null
    private var sLayoutContent: ScrollView? = null
    private var showTitle = false
    private var sheetItemList: MutableList<SheetItem> = mutableListOf()
    private val display: Display

    init {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    @Suppress("DEPRECATION")
    @SuppressLint("InflateParams", "RtlHardcoded")
    fun builder(): ActionSheetDialog {
        sheetItemList.clear()
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
            R.layout.view_actionsheet, null
        )

        // 设置Dialog最小宽度为屏幕宽度
        view.minimumWidth = display.width

        // 获取自定义Dialog布局中的控件
        sLayoutContent = view.findViewById<View>(R.id.sLayout_content) as ScrollView
        lLayoutContent = view
            .findViewById<View>(R.id.lLayout_content) as LinearLayout
        txtTitle = view.findViewById<View>(R.id.txt_title) as TextView
        txtCancel = view.findViewById<View>(R.id.txt_cancel) as TextView
        txtCancel?.setOnClickListener { dialog?.dismiss() }

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog?.setContentView(view)
        val dialogWindow = dialog?.window
        dialogWindow?.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        val lp = dialogWindow?.attributes
        lp?.x = 0
        lp?.y = 0
        dialogWindow?.attributes = lp

        return this
    }

    fun setTitle(title: String): ActionSheetDialog {
        showTitle = true
        txtTitle?.visibility = View.VISIBLE
        txtTitle?.text = title
        return this
    }

    fun setCancelable(cancel: Boolean): ActionSheetDialog {
        dialog?.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): ActionSheetDialog {
        dialog?.setCanceledOnTouchOutside(cancel)
        return this
    }

    /**
     *
     * @param strItem
     * 条目名称
     * @param color
     * 条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    fun addSheetItem(
        strItem: String, colorRes: Int,
        listener: OnSheetItemClickListener
    ): ActionSheetDialog {
        sheetItemList.add(SheetItem(strItem, colorRes, listener))
        return this
    }

    /** 设置条目布局  */
    @Suppress("DEPRECATION")
    private fun setSheetItems() {
        if (sheetItemList.size <= 0) {
            return
        }

        val size = sheetItemList.size

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            val params = sLayoutContent?.layoutParams as LayoutParams
            params.height = display.height / 2
            sLayoutContent?.layoutParams = params
        }

        // 循环添加条目
        for (i in 1..size) {
            val sheetItem = sheetItemList[i - 1]
            val strItem = sheetItem.name
            val colorResource = sheetItem.colorRes

            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 18f
            textView.gravity = Gravity.CENTER

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector)
                }
            } else {
                if (showTitle) {
                    if (i in 1..(size - 1)) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                    }
                } else {
                    when {
                        i == 1 -> textView.setBackgroundResource(R.drawable.actionsheet_top_selector)
                        i < size -> textView.setBackgroundResource(R.drawable.actionsheet_middle_selector)
                        else -> textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                    }
                }
            }

            // 字体颜色
            textView.setTextColor(Config.getColor(colorResource))

            // 高度
            val scale = context.resources.displayMetrics.density
            val height = (45 * scale + 0.5f).toInt()
            textView.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, height
            )

            // 点击事件
            textView.setOnClickListener {
                sheetItem.itemClickListener.onClick(i)
                dialog?.dismiss()
            }

            lLayoutContent?.addView(textView)
        }
    }

    fun show() {
        setSheetItems()
        dialog?.show()
    }

    interface OnSheetItemClickListener {
        fun onClick(which: Int)
    }

    inner class SheetItem(
        var name: String, var colorRes: Int,
        var itemClickListener: OnSheetItemClickListener
    )

}
