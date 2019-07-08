package com.suncity.dailynotices.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.suncity.dailynotices.callback.NetworkMonitor
import com.suncity.dailynotices.manager.ScreenManager
import com.suncity.dailynotices.receive.NetworkChangedReceiver
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreferenceStorage
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui
 * @ClassName:      BaseActivity
 * @Description:    所有 activity 类的基类
 */
abstract class BaseActivity : AppCompatActivity() {

    var isFullScreen = false//是否允许全屏
    var isScreenPortrait = true//是否禁止旋转屏幕
    var netWorkRegister : NetworkChangedReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.e("${this.javaClass} onCreate")
        setScreenManager()
        ScreenManager.setScreenRoate(isScreenPortrait,this)
        ScreenManager.setFullScreen(isFullScreen,this)
        val contentView = getActivityContentView()
        if(contentView == null){
            setContentView(getActivityLayoutId())
        }else{
            setContentView(contentView)
        }
        registerNetReceiver()
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("${this.javaClass} onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.e("${this.javaClass} onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.e("${this.javaClass} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.e("${this.javaClass} onDestroy")
        unRegisterNetReceiver()
    }

    protected fun isLogined(): Boolean {
        return PreferenceStorage.isLogin
    }

    @Suppress("DEPRECATION")
    private fun registerNetReceiver(){
        LogUtils.e("registerNetReceiver -> $netWorkRegister")
        if(netWorkRegister == null){
            netWorkRegister = NetworkChangedReceiver()
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(netWorkRegister,intentFilter)
        }
        netWorkRegister?.setNetworkMonitor(object : NetworkMonitor{
            override fun onNetDisconnected() {
                onNetDis()
            }

            override fun onWifeConnected() {
                onWifiConn()
            }

            override fun onMobileConnected() {
                onMobileConn()
            }

        })
    }

    private fun unRegisterNetReceiver(){
        if (netWorkRegister != null){
            unregisterReceiver(netWorkRegister)
            netWorkRegister?.setNetworkMonitor(null)
        }

    }

    fun onNetDis(){}
    fun onMobileConn(){}
    fun onWifiConn(){}

    /**
     * 关于手机屏幕的一些操作处理在此方法中
     */
    open fun setScreenManager() {}

    abstract fun getActivityLayoutId():Int

    fun getActivityContentView():View?{
        return null
    }

    /**
     * 初始化数据，请求网络数据
     */

    open fun initListener() {}

    open fun initData(){}

    @SuppressLint("CheckResult")
    protected fun requestPermissions(vararg list: String, result: (pass: Boolean) -> Unit) {
        RxPermissions(this).request(*list).subscribe({ result(it) }, { result(false) })
    }

    /**
     * @param cls 需要打开的页面 class
     * @param isFinish 打开之前是否需要关闭当前页面
     */
    fun startActivity(cls: Class<*>, isFinish: Boolean = false) {
        startActivity(cls, -1, isFinish)
    }

    /**
     * @param cls 需要打开的页面 class
     * @param requestCode 需要传递过去的 requestCode
     * @param isFinish 打开之前是否需要关闭当前页面
     */
    fun startActivity(cls: Class<*>, requestCode: Int = -1, isFinish: Boolean = false) {
        val intent = Intent(this, cls)
        if (requestCode == -1) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
        if (isFinish) {
            finish()
        }
    }
}