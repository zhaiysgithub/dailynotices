package com.suncity.dailynotices.manager

import android.content.pm.ActivityInfo
import android.view.Window
import android.view.WindowManager
import com.suncity.dailynotices.ui.BaseActivity

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.manager
 * @ClassName:      ScreenManager
 * @Description:    屏幕管理类
 * @UpdateDate:     31/5/2019
 */
object ScreenManager {

    /**
     * 窗口是否全屏显示
     */
    fun setFullScreen(isChange: Boolean, activity: BaseActivity) {
        if (!isChange) {
            return
        }
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }


    /**
     * 旋转屏幕
     */
    fun setScreenRoate(isPortrait: Boolean, activity: BaseActivity) {
        if(isPortrait){
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}