package com.suncity.dailynotices.utils

import android.annotation.SuppressLint
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
object DateUtils{


    fun formatDateToM(date:Date):String{
        return try {
            val dateInstance = SimpleDateFormat("MM-dd/HH:mm")
            dateInstance.format(date)
        }catch (e : Exception){
            ""
        }

    }
}