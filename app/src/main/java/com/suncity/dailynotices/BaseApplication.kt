package com.suncity.dailynotices

import android.app.Application
import com.avos.avoscloud.AVOSCloud
import com.suncity.dailynotices.utils.AppUtils
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices
 * @ClassName:      BaseApplication
 * @Description:     作用描述
 * @UpdateDate:     31/5/2019
 */

class BaseApplication : Application(){


    override fun onCreate() {
        super.onCreate()
        Config.setApplicationContext(this)
        AppUtils.init(this)
        initAvos()
    }


    // 程序终止的时候执行
    override fun onTerminate() {
        LogUtils.e("BaseApplication -> onTerminate")
        super.onTerminate()
    }

    //低内存的时候执行
    override fun onLowMemory() {
        LogUtils.e("BaseApplication -> onLowMemory")
        super.onLowMemory()
    }

    //程序在内存清理的时候执行
    override fun onTrimMemory(level: Int) {
        LogUtils.e("BaseApplication -> onTrimMemory")
        super.onTrimMemory(level)
    }

    private fun initAvos() {
        val avosAppKey = AppEnviroment.getEnv()?.avosAppKey
        val avosAppId = AppEnviroment.getEnv()?.avosAppId
        LogUtils.e("initAvos -> avosAppKey=$avosAppKey,avosAppId=$avosAppId ")
        AVOSCloud.initialize(this,avosAppId,avosAppKey)
        AVOSCloud.setDebugLogEnabled(true) //打开可调式的开关
    }
}