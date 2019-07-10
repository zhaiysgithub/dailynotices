package com.suncity.dailynotices.ui.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.model
 * @ClassName:      SettingModel
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingModel(text: String, value: String) : Serializable{

    // 文本定义
    var text_set : String? = text

    // 文字描述
    var value_set : String? = value

}