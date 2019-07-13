package com.suncity.dailynotices.utils

import android.annotation.SuppressLint
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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

    fun compareDate(endTime: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val endTimeDate = format.parse(endTime)
            val endTimeLong = endTimeDate.time
            val currentTimeMillis = System.currentTimeMillis()
            (endTimeLong < currentTimeMillis)
        }catch (e : Exception){
            false
        }
    }

    fun compareDate(time1:String,time2:String):Boolean{
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val timeDate1 = format.parse(time1)
            val timeDate2 = format.parse(time2)
            val endTimeLong1 = timeDate1.time
            val endTimeLong2 = timeDate2.time
            (endTimeLong1 > endTimeLong2)
        }catch (e : Exception){
            false
        }
    }

    fun compareDate(time1:Date?,time2:Date?):Boolean{
        return try {
            val endTimeLong1 = time1?.time ?: 0L
            val endTimeLong2 = time2?.time ?: 0L
            (endTimeLong1 > endTimeLong2)
        }catch (e : Exception){
            false
        }
    }
}