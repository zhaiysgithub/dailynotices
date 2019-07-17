package com.suncity.dailynotices.callback

import com.suncity.dailynotices.observer.ObserverList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices
 * @ClassName:      GlobalObserverHelper
 * @Description:    监听全局分发通知的工具类
 */
object GlobalObserverHelper {

    private val mObservers = ObserverList<GlobalObservable>()

    fun addObserver(observer: GlobalObservable) {
        if (mObservers.hasObserver(observer)) {
            return
        }
        mObservers.addObserver(observer)
    }


    fun removeObserver(observer: GlobalObservable) {
        if (!mObservers.hasObserver(observer)) {
            return
        }
        mObservers.removeObserver(observer)
    }

    fun loginSuccess(){
        mObservers.forEach {
            it.onLoginSuccess()
        }
    }

    fun logoutSuccess(){
        mObservers.forEach {
            it.onLogoutSuccess()
        }
    }

    fun updateUserInfo(){
        mObservers.forEach {
            it.onUpdateUserinfoSuccess()
        }
    }


}