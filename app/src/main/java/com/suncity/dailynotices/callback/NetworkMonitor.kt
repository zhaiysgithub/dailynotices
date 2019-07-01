package com.suncity.dailynotices.callback

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      NetworkMonitor
 * @Description:     网络监听类
 */
interface NetworkMonitor{

    /**
     * 网络断开的时间发送通知
     */
    fun onNetDisconnected()

    /**
     * WIFI网络状态连接上发送通知
     */
    fun onWifeConnected()

    /**
     * mobile网络状态连接上发送通知
     */
    fun onMobileConnected()
}