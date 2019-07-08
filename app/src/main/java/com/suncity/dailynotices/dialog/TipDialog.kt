package com.suncity.dailynotices.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.dialog.DialogSettings.Companion.THEME_LIGHT
import com.suncity.dailynotices.dialog.DialogSettings.Companion.blur_alpha
import com.suncity.dailynotices.dialog.DialogSettings.Companion.tipTextInfo
import com.suncity.dailynotices.dialog.DialogSettings.Companion.tip_theme
import com.suncity.dailynotices.utils.Config
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      TipDialog
 * @Description:     作用描述
 * @UpdateDate:     8/7/2019
 */

class TipDialog private constructor() : BaseDialog() {


    private var alertDialog: AlertDialog? = null
    private var tipDialog: TipDialog? = null
    private var customDrawable: Drawable? = null
    private var customBitmap: Bitmap? = null
    private var isCanCancel = false

    private var customTextInfo: TextInfo? = null

    private var context: Context = Config.getApplicationContext()
    private var tip: String? = ""

    private var boxInfo: RelativeLayout? = null
    private var boxBkg: RelativeLayout? = null
    private var image: ImageView? = null
    private var txtInfo: TextView? = null

    private var blur_front_color: Int = 0
    private var font_color: Int = 0

    private var howLong = 0
    private var type = 0

    private var dialogHelper: DialogHelper? = null

    companion object {
        const val SHOW_TIME_SHORT = 0
        const val SHOW_TIME_LONG = 1

        const val TYPE_CUSTOM_DRAWABLE = -2
        const val TYPE_CUSTOM_BITMAP = -1
        const val TYPE_WARNING = 0
        const val TYPE_ERROR = 1
        const val TYPE_FINISH = 2

        fun show(context: Context, tip: String): TipDialog {
            val tipDialog = build(context, tip, SHOW_TIME_SHORT, TYPE_WARNING)
            tipDialog.showDialog()
            return tipDialog
        }

        fun show(context: Context, tip: String, type: Int): TipDialog {
            val tipDialog = build(context, tip, SHOW_TIME_SHORT, type)
            tipDialog.showDialog()
            return tipDialog
        }

        fun show(context: Context, tip: String, howLong: Int, type: Int): TipDialog {
            val tipDialog = build(context, tip, howLong, type)
            tipDialog.showDialog()
            return tipDialog
        }
        fun build(context: Context, tip: String, howLong: Int, type: Int): TipDialog {
            synchronized(TipDialog::class.java) {
                val tipDialog = TipDialog()
                tipDialog.cleanDialogLifeCycleListener()
                tipDialog.context = context
                tipDialog.tip = tip
                tipDialog.howLong = howLong
                tipDialog.type = type
                tipDialog.tipDialog = tipDialog
                return tipDialog
            }
        }

        fun show(context: Context, tip: String, howLong: Int, customDrawable: Drawable): TipDialog {
            val tipDialog = build(context, tip, howLong, customDrawable)
            tipDialog.showDialog()
            return tipDialog
        }

        fun build(context: Context, tip: String, howLong: Int, customDrawable: Drawable): TipDialog {
            synchronized(TipDialog::class.java) {
                val tipDialog = TipDialog()
                tipDialog.cleanDialogLifeCycleListener()
                tipDialog.context = context
                tipDialog.tip = tip
                tipDialog.customDrawable = customDrawable
                tipDialog.howLong = howLong
                tipDialog.type = TYPE_CUSTOM_DRAWABLE
                tipDialog.tipDialog = tipDialog
                return tipDialog
            }
        }

        fun show(context: Context, tip: String, howLong: Int, customBitmap: Bitmap): TipDialog {
            val tipDialog = build(context, tip, howLong, customBitmap)
            tipDialog.showDialog()
            return tipDialog
        }

        fun build(context: Context, tip: String, howLong: Int, customBitmap: Bitmap): TipDialog {
            synchronized(TipDialog::class.java) {
                val tipDialog = TipDialog()
                tipDialog.cleanDialogLifeCycleListener()
                tipDialog.context = context
                tipDialog.tip = tip
                tipDialog.customBitmap = customBitmap
                tipDialog.howLong = howLong
                tipDialog.type = TYPE_CUSTOM_BITMAP
                tipDialog.tipDialog = tipDialog
                return tipDialog
            }
        }
    }

    override fun showDialog() {
        if (customTextInfo == null) {
            customTextInfo = tipTextInfo
        }

        val builder: AlertDialog.Builder
        if (tipDialog != null){
            dialogList.add(tipDialog!!)
        }

        val bkgResId: Int
        when (tip_theme) {
            THEME_LIGHT -> {
                builder = AlertDialog.Builder(context, R.style.lightMode)
                bkgResId = R.drawable.rect_light
                blur_front_color = Color.argb(blur_alpha - 50, 255, 255, 255)
                font_color = Color.rgb(0, 0, 0)
            }
            else -> {
                builder = AlertDialog.Builder(context, R.style.darkMode)
                bkgResId = R.drawable.rect_dark
                blur_front_color = Color.argb(blur_alpha, 0, 0, 0)
                font_color = Color.rgb(255, 255, 255)
            }
        }

        alertDialog = builder.create()
        getDialogLifeCycleListener().onCreate(alertDialog!!)
        if (isCanCancel) alertDialog?.setCanceledOnTouchOutside(true)

        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        dialogHelper = DialogHelper().setAlertDialog(alertDialog!!, object : OnDismissListener{
            override fun onDismiss() {
                if (tipDialog != null && dialogList.contains(tipDialog!!)){
                    dialogList.remove(tipDialog!!)
                }
                if (boxBkg != null) boxBkg?.removeAllViews()
                getDialogLifeCycleListener().onDismiss()
                getOnDismissListener()?.onDismiss()
            }

        })
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_tip, null)
        alertDialog!!.setView(rootView)

        boxInfo = rootView.findViewById(R.id.box_info)
        boxBkg = rootView.findViewById(R.id.box_bkg)
        image = rootView.findViewById(R.id.image)
        txtInfo = rootView.findViewById(R.id.txt_info)

        txtInfo?.setTextColor(font_color)

        boxInfo?.setBackgroundResource(bkgResId)

        when (type) {
            TYPE_WARNING -> if (tip_theme == THEME_LIGHT) {
                image?.setImageResource(R.mipmap.ico_img_warning_dark)
            } else {
                image?.setImageResource(R.mipmap.ico_img_warning)
            }
            TYPE_ERROR -> if (tip_theme == THEME_LIGHT) {
                image?.setImageResource(R.mipmap.ico_img_error_dark)
            } else {
                image?.setImageResource(R.mipmap.ico_img_error)
            }
            TYPE_FINISH -> if (tip_theme == THEME_LIGHT) {
                image?.setImageResource(R.mipmap.ico_img_finish_dark)
            } else {
                image?.setImageResource(R.mipmap.ico_img_finish)
            }
            TYPE_CUSTOM_BITMAP -> image?.setImageBitmap(customBitmap)
            TYPE_CUSTOM_DRAWABLE -> image?.setImageDrawable(customDrawable)
        }

        if (tip?.isEmpty() == false) {
            boxInfo?.visibility = View.VISIBLE
            txtInfo?.text = tip
        } else {
            boxInfo?.visibility = View.GONE
        }

        val fontSize = customTextInfo?.getFontSize() ?: 0
        if (fontSize > 0) {
            txtInfo?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
        }
        val fontColor = customTextInfo?.getFontColor() ?: 1
        if (fontColor != 1) {
            txtInfo?.setTextColor(fontColor)
        }
        val gravity = customTextInfo?.getGravity() ?: -1
        if (gravity != -1) {
            txtInfo?.gravity = gravity
        }
        val font = Typeface.create(Typeface.SANS_SERIF, if (customTextInfo?.isBold() == true) Typeface.BOLD else Typeface.NORMAL)
        txtInfo?.typeface = font

        getDialogLifeCycleListener().onShow(alertDialog!!)

        var time = 1500
        when (howLong) {
            SHOW_TIME_SHORT -> time = 1500
            SHOW_TIME_LONG -> time = 3000
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (dialogHelper != null) dialogHelper?.dismiss()
            }
        }, time.toLong())

        dialogHelper?.show(fragmentManager, "kongzueDialog")
        dialogHelper?.isCancelable = isCanCancel
    }

    override fun doDismiss() {
        if (dialogHelper != null) dialogHelper?.dismiss()
    }

    fun setCanCancel(canCancel: Boolean): TipDialog {
        isCanCancel = canCancel
        if (dialogHelper != null) dialogHelper?.setCancelable(canCancel)
        return this
    }

    fun setTxtInfo(txtInfo: TextView): TipDialog {
        this.txtInfo = txtInfo
        return this
    }

    fun getAlertDialog(): AlertDialog? {
        return alertDialog
    }
}