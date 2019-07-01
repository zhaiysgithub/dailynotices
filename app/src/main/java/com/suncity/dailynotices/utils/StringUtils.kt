package com.suncity.dailynotices.utils

import android.app.Activity
import android.graphics.Color
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      StringUtils
 * @Description:    对 String 字符串做一些条件筛选
 */

object StringUtils{

    fun <T> checkNotNull(value:T):T{
        if (value == null){
            LogUtils.e("$value == null")
        }
        return value
    }

    fun isEmpty(text: String?): Boolean {

        return null == text || TextUtils.isEmpty(text) || text == "null"
    }

    fun isPhoneNumber(context: Activity, number: String): Boolean {
        //"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        val num = "[1][3578]\\d{9}"
        return if (TextUtils.isEmpty(number)) {
            ToastUtils.showSafeToast(context, "请输入号码")
            false
        } else {
            number.matches(num.toRegex())
        }
    }

    fun isEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        val p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")//复杂匹配
        val m = p.matcher(email)
        return m.matches()
    }


    fun parseColor(colorStr: String?): Int {
        return try {
            if (isEmptyOrNull(colorStr)) {
                0
            } else {
                Color.parseColor(colorStr)
            }
        } catch (e: Exception) {
            0
        }
    }

    fun isNotEmptyAndNull(str: String?): Boolean {
        return str != null && !TextUtils.isEmpty(str) && str.isNotEmpty() && str != "null"
    }

    fun isEmptyOrNull(str: String?): Boolean {
        return null == str || TextUtils.isEmpty(str) || str.isEmpty() || str == "null"
    }
}