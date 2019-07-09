package com.suncity.dailynotices.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import java.lang.Exception

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      SharedPrefHelper
 * @Description:     SP 保存对象
 * @UpdateDate:     9/7/2019
 */
@SuppressLint("CommitPrefEdits")
class SharedPrefHelper {

    companion object {

        /**
         * 保存对象到 sp 中
         */

        fun saveAny(key: String, value: Any) {

            try {
                val sp = Config.getApplicationContext().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE)
                val editor = sp.edit()
                val gson = Gson()
                val json = gson.toJson(value)
                editor.putString(key, json)
                editor.apply()
            } catch (e: Exception) {
                LogUtils.e("saveAny-exception -> key=$key,e->$e")

            }
        }


        fun retireveAny(key: String): String? {

            return try {
                val sp = Config.getApplicationContext().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE)
                val jsonStr = sp.getString(key, null)
                jsonStr
            } catch (e: Exception) {
                LogUtils.e("retireveAny-exception -> key=$key,e->$e")
                null
            }
        }
    }
}