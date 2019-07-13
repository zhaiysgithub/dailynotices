package com.suncity.dailynotices.callback

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      OnDynamicItemMenuClick
 * @Description:     动态 item 的点击事件接口
 * @UpdateDate:     13/7/2019
 */
interface OnDynamicItemMenuClick {

    fun onMoreClick(position: Int)

    fun onImageClick(position:Int,url:String)

    fun onSelectLikeClick(position:Int)

    fun onTagFlowClick(position:Int,tagPos:Int,tagString:String)

}