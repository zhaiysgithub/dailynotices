package com.suncity.dailynotices.manager

import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.utils.PreferenceStorage

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.manager
 * @ClassName:      UserManager
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */

object UserManager{

    fun removeUserInfo(){
        PreferenceStorage.isLogin = false
        PreferenceStorage.clear()
        GlobalObserverHelper.logoutSuccess()
    }
}