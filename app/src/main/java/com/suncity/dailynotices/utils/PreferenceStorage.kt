package com.suncity.dailynotices.utils

import android.content.Context
import android.content.SharedPreferences
import com.suncity.dailynotices.Constants

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      PreferenceStorage
 * @Description:    本地 SP 保存的 kv
 */

object PreferenceStorage {

    private val sharedPreferences: SharedPreferences = Config.getApplicationContext().getSharedPreferences("dailynotice", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    var isLogin : Boolean
        @JvmStatic
        get() = sharedPreferences.getBoolean(Constants.ISLOGINED,false)
        @JvmStatic
        set(value) {
            editor.putBoolean(Constants.ISLOGINED,value)
            editor.commit()
        }
}