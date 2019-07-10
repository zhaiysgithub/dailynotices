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

    var userObjectId : String
        @JvmStatic
        get() = (sharedPreferences.getString(Constants.USEROBJECTID,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(Constants.USEROBJECTID,value)
            editor.commit()
        }

    var userName:String
        @JvmStatic
        get() = (sharedPreferences.getString(Constants.USERNAME,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(Constants.USERNAME,value)
            editor.commit()
        }

    var userAvatar:String
        @JvmStatic
        get() = (sharedPreferences.getString(Constants.USERAvatar,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(Constants.USERAvatar,value)
            editor.commit()
        }

}