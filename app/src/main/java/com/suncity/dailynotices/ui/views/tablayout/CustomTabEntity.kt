package com.suncity.dailynotices.ui.views.tablayout

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.views.tablayout
 * @ClassName:      CustomTabEntity
 * @Description:    tab 实例
 */
interface CustomTabEntity {

    fun getTabTitle():String

    fun getTabSelectedIcon():Int

    fun getTabUnselectedIcon():Int
}