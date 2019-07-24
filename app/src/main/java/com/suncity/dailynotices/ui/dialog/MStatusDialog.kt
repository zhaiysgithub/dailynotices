package com.suncity.dailynotices.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.DisplayUtils

/**
 * 提示Dialog
 */
class MStatusDialog @JvmOverloads constructor(
    private var mContext: Context,
    private var mDialogConfig: MDialogConfig = MDialogConfig.Builder().build()
) {

    private var mHandler: Handler? = null
    private var mDialog: Dialog? = null

    private var dialogWindowBackground: RelativeLayout? = null
    private var dialogViewBg: RelativeLayout? = null
    private var imageStatus: ImageView? = null
    private var tvShow: TextView? = null

    init {
        mHandler = Handler(Looper.getMainLooper())
        initDialog()
    }

    private fun initDialog() {

        val inflater = LayoutInflater.from(mContext)
        val mProgressDialogView = inflater.inflate(R.layout.mn_status_dialog_layout, null)
        mDialog = Dialog(mContext, R.style.MNCustomDialog)
        mDialog?.setCancelable(false)
        mDialog?.setCanceledOnTouchOutside(false)
        mDialog?.setContentView(mProgressDialogView)

        //设置整个Dialog的宽高
        val layoutParams = mDialog?.window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.gravity = Gravity.CENTER
        mDialog?.window?.attributes = layoutParams

        //获取布局
        dialogWindowBackground =
            mProgressDialogView.findViewById<View>(R.id.dialog_window_background) as RelativeLayout
        dialogViewBg = mProgressDialogView.findViewById<View>(R.id.dialog_view_bg) as RelativeLayout
        imageStatus = mProgressDialogView.findViewById<View>(R.id.imageStatus) as ImageView
        tvShow = mProgressDialogView.findViewById<View>(R.id.tvShow) as TextView

        //默认配置
        configView()

    }

    private fun checkDialogConfig() {
        if (mDialogConfig == null) {
            mDialogConfig = MDialogConfig.Builder().build()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    private fun configView() {
        checkDialogConfig()
        //window背景
        dialogWindowBackground?.setBackgroundColor(mDialogConfig.backgroundWindowColor)

        //文字设置
        tvShow?.setTextColor(mDialogConfig.textColor)
        tvShow?.textSize = mDialogConfig.textSize

        //弹框背景
        val myGrad = GradientDrawable()
        myGrad.setColor(mDialogConfig.backgroundViewColor)
        myGrad.setStroke(DisplayUtils.dp2px(mDialogConfig.strokeWidth).toInt(), mDialogConfig.strokeColor)
        myGrad.cornerRadius = DisplayUtils.dp2px(mDialogConfig.cornerRadius)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dialogViewBg?.background = myGrad
        } else {
            dialogViewBg?.setBackgroundDrawable(myGrad)
        }
        dialogViewBg?.setPadding(
            DisplayUtils.dp2px(mDialogConfig.paddingLeft).toInt(),
            DisplayUtils.dp2px(mDialogConfig.paddingTop).toInt(),
            DisplayUtils.dp2px(mDialogConfig.paddingRight).toInt(),
            DisplayUtils.dp2px(mDialogConfig.paddingBottom).toInt()
        )

        //设置动画
        try {
            if (mDialogConfig.animationID != 0 && mDialog?.window != null) {
                mDialog?.window?.setWindowAnimations(mDialogConfig.animationID)
            }
        } catch (e: Exception) {

        }

        //图片宽高
        if (mDialogConfig.imgWidth > 0 && mDialogConfig.imgHeight > 0) {
            val layoutParams = imageStatus?.layoutParams
            layoutParams?.width = DisplayUtils.dp2px(mDialogConfig.imgWidth).toInt()
            layoutParams?.height = DisplayUtils.dp2px(mDialogConfig.imgHeight).toInt()
            imageStatus?.layoutParams = layoutParams
        }

        //全屏模式
        if (mDialogConfig.windowFullscreen) {
            mDialog?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

    }

    @JvmOverloads
    fun show(msg: String, drawable: Drawable, delayMillis: Long = 2000) {
        imageStatus?.setImageDrawable(drawable)
        tvShow?.text = msg
        mDialog?.show()
        mHandler?.postDelayed({ dismiss() }, delayMillis)
    }

    fun dismiss() {
        try {
            if (mHandler != null) {
                mHandler!!.removeCallbacksAndMessages(null)
                mHandler = null
            }
            if (mDialog != null) {
                mDialog!!.dismiss()
                mDialog = null
            }
            if (mDialogConfig.onDialogDismissListener != null) {
                mDialogConfig.onDialogDismissListener!!.onDismiss()
            }
        } catch (e: Exception) {

        }

    }
}
