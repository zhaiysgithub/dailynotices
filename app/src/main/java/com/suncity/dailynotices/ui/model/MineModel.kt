package com.suncity.dailynotices.ui.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.model
 * @ClassName:      MineModel
 * @Description:    我的模块底部的列表
 */

class MineModel(positon:Int,ivResId: Int, text: String) : Serializable{

    // 图片资源
    var imageResId : Int? = ivResId

    // 文字描述
    var desc : String? = text

}