package com.suncity.dailynotices.ui.dialog

import android.graphics.Color
import androidx.annotation.StyleRes
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.Config


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.dialog
 * @ClassName:      MDialogConfig
 * @Description:    dialog 配置
 * @UpdateDate:     24/7/2019
 */
class MDialogConfig private constructor() {

    //全屏模式隐藏状态栏
    var windowFullscreen = false
    //点击外部可以取消
    var canceledOnTouchOutside = false
    //是否可以返回键关闭
    var cancelable = false
    //窗体背景色
    var backgroundWindowColor = Color.TRANSPARENT
    //View背景色
    var backgroundViewColor = Config.getColor(R.color.color_b2000)
    //View边框的颜色
    var strokeColor = Color.TRANSPARENT
    //View背景圆角
    var cornerRadius = 8f
    //View边框的宽度
    var strokeWidth = 0f
    //Progress的颜色
    var progressColor = Color.WHITE
    //Progress的宽度
    var progressWidth = 2f
    //ProgressSize(dp)
    var progressSize = 45f
    //progress背景环的颜色
    var progressRimColor = Color.TRANSPARENT
    //progress背景环的宽度
    var progressRimWidth = 0
    //文字的颜色
    var textColor = Color.WHITE
    //文字的大小:默认12sp
    var textSize = 12f
    //消失的监听
    var onDialogDismissListener: OnDialogDismissListener? = null
    //Dialog进出动画
    var animationID = 0
    //布局的Padding--int left, int top, int right, int bottom
    var paddingLeft = 12f
    var paddingTop = 12f
    var paddingRight = 12f
    var paddingBottom = 12f

    //StatusDialog专用：中间图片宽高
    var imgWidth = 40f
    var imgHeight = 40f

    class Builder {

        private val mDialogConfig: MDialogConfig = MDialogConfig()

        fun build(): MDialogConfig {
            return mDialogConfig
        }

        fun isCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            mDialogConfig.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun isWindowFullscreen(windowFullscreen: Boolean): Builder {
            mDialogConfig.windowFullscreen = windowFullscreen
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            mDialogConfig.cancelable = cancelable
            return this
        }

        fun setBackgroundWindowColor(backgroundWindowColor: Int): Builder {
            mDialogConfig.backgroundWindowColor = backgroundWindowColor
            return this
        }

        fun setBackgroundViewColor(backgroundViewColor: Int): Builder {
            mDialogConfig.backgroundViewColor = backgroundViewColor
            return this
        }

        fun setStrokeColor(strokeColor: Int): Builder {
            mDialogConfig.strokeColor = strokeColor
            return this
        }

        fun setStrokeWidth(strokeWidth: Float): Builder {
            mDialogConfig.strokeWidth = strokeWidth
            return this
        }

        fun setCornerRadius(cornerRadius: Float): Builder {
            mDialogConfig.cornerRadius = cornerRadius
            return this
        }

        fun setProgressColor(progressColor: Int): Builder {
            mDialogConfig.progressColor = progressColor
            return this
        }

        fun setProgressWidth(progressWidth: Float): Builder {
            mDialogConfig.progressWidth = progressWidth
            return this
        }

        fun setProgressRimColor(progressRimColor: Int): Builder {
            mDialogConfig.progressRimColor = progressRimColor
            return this
        }

        fun setProgressRimWidth(progressRimWidth: Int): Builder {
            mDialogConfig.progressRimWidth = progressRimWidth
            return this
        }

        fun setProgressSize(progressSize: Int): Builder {
            mDialogConfig.progressSize = progressSize.toFloat()
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            mDialogConfig.textColor = textColor
            return this
        }

        fun setTextSize(textSize: Float): Builder {
            mDialogConfig.textSize = textSize
            return this
        }

        fun setOnDialogDismissListener(onDialogDismissListener: OnDialogDismissListener): Builder {
            mDialogConfig.onDialogDismissListener = onDialogDismissListener
            return this
        }

        fun setAnimationID(@StyleRes resId: Int): Builder {
            mDialogConfig.animationID = resId
            return this
        }

        fun setImgWidthAndHeight(imgWidth: Float, imgHeight: Float): Builder {
            mDialogConfig.imgWidth = imgWidth
            mDialogConfig.imgHeight = imgHeight
            return this
        }

        fun setPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float): Builder {
            mDialogConfig.paddingLeft = paddingLeft
            mDialogConfig.paddingTop = paddingTop
            mDialogConfig.paddingRight = paddingRight
            mDialogConfig.paddingBottom = paddingBottom
            return this
        }
    }

}
