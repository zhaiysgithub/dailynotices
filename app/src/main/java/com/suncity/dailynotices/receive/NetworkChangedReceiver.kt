package com.suncity.dailynotices.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.suncity.dailynotices.callback.NetworkMonitor
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.NetworkUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.receive
 * @ClassName:      NetworkConnectChangedReceiver
 * @Description:    监听网络变化的广播
 * @UpdateDate:     31/5/2019
 */
class NetworkChangedReceiver : BroadcastReceiver() {

    private var netChangeObserver : NetworkMonitor? = null

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtils.e("NetworkChangedReceiver")
        if (intent != null && context != null && intent.action == ConnectivityManager.CONNECTIVITY_ACTION){
            val netWorkState = NetworkUtils.getNetWorkState(context)
            LogUtils.e("netWorkState -> $netWorkState")
            when(netWorkState){
                NetworkUtils.NETWORK_NONE -> {netChangeObserver?.onNetDisconnected()}
                NetworkUtils.NETWORK_MOBILE -> {netChangeObserver?.onMobileConnected()}
                NetworkUtils.NETWORK_WIFI -> {netChangeObserver?.onWifeConnected()}
            }
        }

    }

    fun setNetworkMonitor(monitor:NetworkMonitor?){
        netChangeObserver = monitor
    }




}