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

    private val sharedPreferences: SharedPreferences =
        Config.getApplicationContext().getSharedPreferences("dailynotice", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private const val isLoginedStr = Constants.ISLOGINED
    private const val userObjcetIdStr = Constants.USEROBJECTID
    private const val userNameStr = Constants.USERNAME
    private const val userPhoneNumStr = Constants.USERPHONENUM
    private const val userAvatarStr = Constants.USERAVATAR
    private const val userIsAutonym = Constants.USERISAUTONYM
    //    private val userAuthName =  "${userObjectId}name"
//    private val userAuthCertficateNum = "${userObjectId}num"
//    private val userAuthCertficatePic = "${userObjectId}pic"
    private const val userAuthName = Constants.USERAUTHNAME
    private const val userAuthCertficateNum = Constants.USERAUTHCERTFICATENUM
    private const val userAuthCertficatePic = Constants.USERAUTHCERTFICATEPIC

    private val needClearData = arrayOf(
        isLoginedStr, userObjcetIdStr, userNameStr
        , userPhoneNumStr, userAvatarStr, userAuthName, userAuthCertficateNum, userAuthCertficatePic
    )

    var authName: String?
        @JvmStatic
        get() = sharedPreferences.getString(userAuthName, "")
        @JvmStatic
        set(value) {
            editor.putString(userAuthName, value)
            editor.commit()
        }

    var authCertficateNum: String?
        @JvmStatic
        get() = sharedPreferences.getString(userAuthCertficateNum, "")
        @JvmStatic
        set(value) {
            editor.putString(userAuthCertficateNum, value)
            editor.commit()
        }

    var authCertficatePic: String?
        @JvmStatic
        get() = sharedPreferences.getString(userAuthCertficatePic, "")
        @JvmStatic
        set(value) {
            editor.putString(userAuthCertficatePic, value)
            editor.commit()
        }

    var isLogin: Boolean
        @JvmStatic
        get() = sharedPreferences.getBoolean(isLoginedStr, false)
        @JvmStatic
        set(value) {
            editor.putBoolean(isLoginedStr, value)
            editor.commit()
        }

    var userObjectId: String
        @JvmStatic
        get() = (sharedPreferences.getString(userObjcetIdStr, "") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userObjcetIdStr, value)
            editor.commit()
        }

    var userName: String
        @JvmStatic
        get() = (sharedPreferences.getString(userNameStr, "") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userNameStr, value)
            editor.commit()
        }

    var userPhoneNum: String
        @JvmStatic
        get() = (sharedPreferences.getString(userPhoneNumStr, "") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userPhoneNumStr, value)
            editor.commit()
        }

    var userAvatar: String
        @JvmStatic
        get() = (sharedPreferences.getString(userAvatarStr, "") ?: "")
        @JvmStatic
        set(value) {
            editor.putString(userAvatarStr, value)
            editor.commit()
        }
    //是否认证
    var isAutonym: Int
        @JvmStatic
        get() = (sharedPreferences.getInt(userIsAutonym, 0))
        @JvmStatic
        set(value) {
            editor.putInt(userIsAutonym, value)
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