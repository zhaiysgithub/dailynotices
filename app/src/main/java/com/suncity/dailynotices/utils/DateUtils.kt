package com.suncity.dailynotices.utils

import android.annotation.SuppressLint
import android.util.Log
import com.suncity.dailynotices.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      DateUtils
 * @Description:     作用描述
 * @UpdateDate:     11/7/2019
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {


    fun formatDateToM(date: Date): String {
        return try {
            val dateInstance = SimpleDateFormat("MM-dd/HH:mm")
            dateInstance.format(date)
        } catch (e: Exception) {
            ""
        }

    }

    fun formatDateToYMD(date: Date): String {
        return try {
            val dateInstance = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateInstance.format(date)
        } catch (e: Exception) {
            ""
        }

    }

    fun compareDate(endTime: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val endTimeDate = format.parse(endTime)
            val endTimeLong = endTimeDate.time
            val currentTimeMillis = System.currentTimeMillis()
            (endTimeLong < currentTimeMillis)
        } catch (e: Exception) {
            false
        }
    }

    fun compareDate(time1: String, time2: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val timeDate1 = format.parse(time1)
            val timeDate2 = format.parse(time2)
            val endTimeLong1 = timeDate1.time
            val endTimeLong2 = timeDate2.time
            (endTimeLong1 > endTimeLong2)
        } catch (e: Exception) {
            false
        }
    }

    fun compareDate(time1: Date?, time2: Date?): Boolean {
        return try {
            val endTimeLong1 = time1?.time ?: 0L
            val endTimeLong2 = time2?.time ?: 0L
            (endTimeLong1 > endTimeLong2)
        } catch (e: Exception) {
            false
        }
    }


}

fun Long?.imPostDate(): String {
    val currentTimeMillis = System.currentTimeMillis()
    // 如果传来的时间大于当前时间。。。
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // 判断是今天
    val formatDate = dateFormat.format(this)
    val currentFormatDate = dateFormat.format(currentTimeMillis)
    if (formatDate == currentFormatDate) {
        val time = currentTimeMillis - (this ?: return "")
        if (time >= 60 * 60 * 1000) return "${time / (60 * 60 * 1000)}${Config.getString(R.string.str_date_old_hours)}"
        if (time >= 60 * 1000) return "${time / (60 * 1000)}${Config.getString(R.string.str_date_old_minutes)}"
        return Config.getString(R.string.str_date_old_was)
    }
    // 判断是昨天
    if (dateFormat.format((this ?: return "") + 60 * 60 * 24 * 1000) == currentFormatDate) {
        return Config.getString(R.string.str_date_yesterday) + SimpleDateFormat("HH:mm", Locale.getDefault()).format(
            this
        )
    }
    // 如果不是同一年
    if (formatDate.substring(0..4) != currentFormatDate.substring(0..4)) return SimpleDateFormat(
        "yyyy-MM-dd HH:mm",
        Locale.getDefault()
    ).format(this)
    // 如果是同一年
    return SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(this)
}