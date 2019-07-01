package com.suncity.dailynotices.utils

import android.content.Context
import android.net.ConnectivityManager
import com.suncity.dailynotices.Constants


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      NetworkUtils
 * @Description:    网络工具类
 * @UpdateDate:
 */

object NetworkUtils{

    /**
     * 没有网络
     */
    const val NETWORK_NONE = -1
    /**
     * 移动网络
     */
    const val NETWORK_MOBILE = 0
    /**
     * 无线网络
     */
    const val NETWORK_WIFI = 1


    @Suppress("DEPRECATION")
    fun getNetWorkState(context: Context): Int {
        //得到连接管理器对象
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取WIFI连接的信息
        val wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return when{
            wifiNetworkInfo.isConnected -> NETWORK_WIFI
            !wifiNetworkInfo.isConnected && mobileNetworkInfo.isConnected -> NETWORK_MOBILE
            !wifiNetworkInfo.isConnected && !mobileNetworkInfo.isConnected -> NETWORK_NONE
            else -> NETWORK_NONE
        }
    }



}