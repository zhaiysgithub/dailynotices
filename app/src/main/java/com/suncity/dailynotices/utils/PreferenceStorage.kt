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

    private const val isLoginedStr = Constants.ISLOGINED
    private const val userObjcetIdStr = Constants.USEROBJECTID
    private const val userNameStr = Constants.USERNAME
    private const val userPhoneNumStr = Constants.USERPHONENUM
    private const val userAvatarStr = Constants.USERAvatar

    private val needClearData = arrayOf(isLoginedStr,userObjcetIdStr,userNameStr,userPhoneNumStr,userAvatarStr)

    var isLogin : Boolean
        @JvmStatic
        get() = sharedPreferences.getBoolean(isLoginedStr,false)
        @JvmStatic
        set(value) {
            editor.putBoolean(isLoginedStr,value)
            editor.commit()
        }

    var userObjectId : String
        @JvmStatic
        get() = (sharedPreferences.getString(userObjcetIdStr,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userObjcetIdStr,value)
            editor.commit()
        }

    var userName:String
        @JvmStatic
        get() = (sharedPreferences.getString(userNameStr,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userNameStr,value)
            editor.commit()
        }

    var userPhoneNum:String
        @JvmStatic
        get() = (sharedPreferences.getString(userPhoneNumStr,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userPhoneNumStr,value)
            editor.commit()
        }

    var userAvatar:String
        @JvmStatic
        get() = (sharedPreferences.getString(userAvatarStr,"") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userAvatarStr,value)
            editor.commit()
        }

    @JvmStatic
    fun clear() {
        needClearData.forEach {
            editor.remove(it)
        }
        editor.commit()
        SharedPrefHelper.needClearSpValue()
    }
}