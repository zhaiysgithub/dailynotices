package com.suncity.dailynotices.utils

import android.util.Log
import com.suncity.dailynotices.BuildConfig

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      LogUtils
 * @Description:    debug 模式下过滤 log 调试
 * @UpdateDate:     30/5/2019
 */

object LogUtils{

    private const val isShowLog = true

    private const val TAG = "DailyNotice"

    fun e(tag:String, msg:String){
        if (isShowLog && BuildConfig.DEBUG){
            Log.e(tag,msg)
        }
    }

    fun e(msg:String){
        if (isShowLog && BuildConfig.DEBUG){
            Log.e(TAG,msg)
        }
    }

}