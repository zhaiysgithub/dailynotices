package com.suncity.dailynotices.utils

import com.suncity.dailynotices.BaseApplication

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      Config
 * @Description:     作用描述
 * @UpdateDate:     1/6/2019
 */

object Config{

    lateinit var context: BaseApplication

    @JvmStatic
    fun setApplicationContext(context: BaseApplication) {
        this.context = context
    }

    @JvmStatic
    fun getApplicationContext(): BaseApplication {
        return context
    }
}
