package com.suncity.dailynotices.utils

import java.text.DecimalFormat

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      ConversionUtils
 * @Description:    转换的工具类
 * @UpdateDate:     23/7/2019
 */
object ConversionUtils {

    fun convertByte2KB(byte:Long):String{
        val gbDefault = 1024 * 1024 * 1024//定义GB的计算常量
        val mbDefault = 1024 * 1024//定义MB的计算常量
        val kbDefault = 1024//定义KB的计算常量
        val format = DecimalFormat("###.00")
        return when {
            byte / gbDefault >= 1 -> //如果当前Byte的值大于等于1GB
                format.format(byte / (gbDefault.toFloat())) + "GB   "
            byte / mbDefault >= 1 -> //如果当前Byte的值大于等于1MB
                format.format(byte /  (mbDefault.toFloat())) + "MB   "
            byte / kbDefault >= 1 -> //如果当前Byte的值大于等于1KB
                format.format(byte /  (kbDefault.toFloat())) + "KB   "
            else -> byte.toString() + "B   "
        }

    }
}