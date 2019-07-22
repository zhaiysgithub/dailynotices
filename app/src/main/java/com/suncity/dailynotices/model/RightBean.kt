package com.suncity.dailynotices.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      RightBean
 * @Description:     作用描述
 * @UpdateDate:     22/7/2019
 */

class RightBean : Serializable {

    var names: ArrayList<String>? = null
    var titleName: String? = null
    var imgsrc: String? = null
    var isSelected:Boolean = false
}