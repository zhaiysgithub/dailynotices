package com.suncity.dailynotices.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.DisplayUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.dialog
 * @ClassName:      MProgressDialogK
 * @Description:     作用描述
 * @UpdateDate:     24/7/2019
 */
@SuppressLint("StaticFieldLeak")
class MProgressDialog {

    companion object {
        private const val LoadingDefaultMsg = "加载中"

        private var mDialog: Dialog? = null
        private var mDialogConfig: MDialogConfig? = null

        private var dialog_window_background: RelativeLayout? = null
        private var dialog_view_bg: RelativeLayout? = null
        private var progress_wheel: MNHudProgressWheel? = null
        private var tv_show: TextView? = null


        private fun initDialog(mContext: Context) {
            val inflater = LayoutInflater.from(mContext)
            val mProgressDialogView = inflater.inflate(R.layout.mn_progress_dialog_layout, null)
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

            //布局相关
            dialog_window_background =
                mProgressDialogView.findViewById<View>(R.id.dialog_window_background) as RelativeLayout
            dialog_view_bg = mProgressDialogView.findViewById<View>(R.id.dialog_view_bg) as RelativeLayout
            progress_wheel = mProgressDialogView.findViewById<View>(R.id.progress_wheel) as MNHudProgressWheel
            tv_show = mProgressDialogView.findViewById<View>(R.id.tv_show) as TextView
            progress_wheel?.spin()

            configView()
        }

        private fun checkDialogConfig() {
            if (mDialogConfig == null) {
                mDialogConfig = MDialogConfig.Builder().build()
            }
        }

        private fun configView() {
            checkDialogConfig()
            //设置动画
            try {
                if (mDialogConfig != null && mDialogConfig?.animationID != 0 && mDialog?.window != null) {
                    mDialog?.window?.setWindowAnimations(mDialogConfig!!.animationID)
                }
            } catch (e: Exception) {

            }

            //点击外部可以取消
            mDialog?.setCanceledOnTouchOutside(mDialogConfig?.canceledOnTouchOutside ?: false)
            //返回键取消
            mDialog?.setCancelable(mDialogConfig?.cancelable ?: false)
            //window背景色
            dialog_window_background?.setBackgroundColor(mDialogConfig?.backgroundWindowColor ?: Color.TRANSPARENT)
            //弹框背景
            val myGrad = GradientDrawable()
            myGrad.setColor(mDialogConfig?.backgroundViewColor ?: Config.getColor(R.color.color_b2000))
            myGrad.setStroke(DisplayUtils.dp2px(mDialogConfig?.strokeWidth ?: 0f).toInt(), mDialogConfig?.strokeColor ?: Color.TRANSPARENT)
            myGrad.cornerRadius = DisplayUtils.dp2px(mDialogConfig?.cornerRadius ?: 8f)
            dialog_view_bg?.background = myGrad
            dialog_view_bg?.setPadding(
                DisplayUtils.dp2px(mDialogConfig?.paddingLeft ?: 12f).toInt(),
                DisplayUtils.dp2px(mDialogConfig?.paddingTop ?: 12f).toInt(),
                DisplayUtils.dp2px(mDialogConfig?.paddingRight ?: 12f).toInt(),
                DisplayUtils.dp2px(mDialogConfig?.paddingBottom ?: 12f).toInt()
            )

            //Progress设置
            progress_wheel?.setBarColor(mDialogConfig?.progressColor ?: Color.WHITE)
            progress_wheel?.setBarWidth(DisplayUtils.dp2px(mDialogConfig?.progressWidth ?: 2f).toInt())
            progress_wheel?.setRimColor(mDialogConfig?.progressRimColor ?: Color.TRANSPARENT)
            progress_wheel?.setRimWidth(mDialogConfig?.progressRimWidth ?: 0)
            val layoutParamsProgress = progress_wheel?.layoutParams
            layoutParamsProgress?.width = DisplayUtils.dp2px(mDialogConfig?.progressSize ?: 45f).toInt()
            layoutParamsProgress?.height = DisplayUtils.dp2px(mDialogConfig?.progressSize ?: 45f).toInt()
            progress_wheel?.layoutParams = layoutParamsProgress
            //文字颜色设置
            tv_show?.setTextColor(mDialogConfig?.textColor ?: Color.WHITE)
            tv_show?.textSize = mDialogConfig?.textSize ?: 12f

            //点击事件
            dialog_window_background?.setOnClickListener {
                //取消Dialog
                if (mDialogConfig != null && mDialogConfig?.canceledOnTouchOutside == true) {
                    dismissProgress()
                }
            }

            //全屏模式
            if (mDialogConfig?.windowFullscreen == true) {
                mDialog?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }

        fun showProgress(context: Context) {
            showProgress(context, LoadingDefaultMsg)
        }

        fun showProgress(context: Context, msg: String) {
            showProgress(context, msg, null)
        }

        fun showProgress(context: Context, mDialogConfig: MDialogConfig) {
            showProgress(context, LoadingDefaultMsg, mDialogConfig)
        }

        fun showProgress(context: Context, msg: String, dialogConfig: MDialogConfig?) {
            var dConfig = dialogConfig
            try {
                dismissProgress()
                //设置配置
                if (dConfig == null) {
                    dConfig = MDialogConfig.Builder().build()
                }
                mDialogConfig = dConfig

                initDialog(context)
                if (mDialog != null && tv_show != null) {
                    if (TextUtils.isEmpty(msg)) {
                        tv_show?.visibility = View.GONE
                    } else {
                        tv_show?.visibility = View.VISIBLE
                        tv_show?.text = msg
                    }
                    mDialog?.show()
                }
            } catch (e: Exception) {

            }

        }

        fun dismissProgress() {
            try {
                if (mDialog != null && mDialog?.isShowing == true) {
                    //判断是不是有监听
                    if (mDialogConfig?.onDialogDismissListener != null) {
                        mDialogConfig?.onDialogDismissListener?.onDismiss()
                        mDialogConfig?.onDialogDismissListener = null
                    }
                    mDialogConfig = null
                    dialog_window_background = null
                    dialog_view_bg = null
                    progress_wheel = null
                    tv_show = null
                    mDialog?.dismiss()
                    mDialog = null
                }
            } catch (e: Exception) {

            }

        }

        fun isShowing(): Boolean {
            return if (mDialog != null) {
                mDialog?.isShowing ?: false
            } else false
        }

    }

}