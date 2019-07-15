package com.suncity.dailynotices.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.suncity.dailynotices.BaseApplication
import com.suncity.dailynotices.R

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      Config
 * @Description:     作用描述
 * @UpdateDate:     1/6/2019
 */

object Config {

    lateinit var context: BaseApplication

    @JvmStatic
    fun setApplicationContext(context: BaseApplication) {
        this.context = context
    }

    @JvmStatic
    fun getApplicationContext(): BaseApplication {
        return context
    }

    @JvmStatic
    fun getResource(): Resources {
        return getApplicationContext().resources
    }

    @JvmStatic
    fun getString(stringId: Int): String {
        return getApplicationContext().getString(stringId)
    }

    @JvmStatic
    fun getString(stringId: Int, vararg s: Any?): String {
        return getApplicationContext().getString(stringId, *s)
    }

    @JvmStatic
    fun getDimension(dimensionId: Int): Float {
        return getApplicationContext().resources.getDimension(dimensionId)
    }

    @JvmStatic
    fun getDrawable(drawableId: Int): Drawable {
        return ContextCompat.getDrawable(getApplicationContext(), drawableId)!!
    }

    @JvmStatic
    fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(getApplicationContext(), colorId)
    }

    @JvmStatic
    fun getResIntArray(arrayId: Int): IntArray {
        return getApplicationContext().resources.getIntArray(arrayId)
    }

    @JvmStatic
    fun getResStringArray(arrayId: Int): Array<String> {
        return getApplicationContext().resources.getStringArray(arrayId)
    }

}
