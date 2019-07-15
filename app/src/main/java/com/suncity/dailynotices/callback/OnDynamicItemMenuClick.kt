package com.suncity.dailynotices.callback

import android.view.View
import com.suncity.dailynotices.model.Dynamic

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      OnDynamicItemMenuClick
 * @Description:     动态 item 的点击事件接口
 * @UpdateDate:     13/7/2019
 */
interface OnDynamicItemMenuClick {

    fun onMoreClick(position: Int)

    fun onImageClick(view: View, position: Int, url: String, data: Dynamic)

    fun onSelectLikeClick(position: Int,data: Dynamic)

    fun onTagFlowClick(position: Int, tagPos: Int, tagString: String)


}