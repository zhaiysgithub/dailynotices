package com.suncity.dailynotices.callback

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      OnTabSelectListener
 * @Description:    Tab 选中的回调监听
 */
interface OnTabSelectListener {

    fun onTabSelect(position:Int)

    fun onTabReselect(position: Int)
}