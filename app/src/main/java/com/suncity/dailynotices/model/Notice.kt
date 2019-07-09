package com.suncity.dailynotices.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Notice
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Notice : Serializable {

    var objectId:String? = null
    var workTime:String? = null
    var payment:String? = null
    var endTime:String? = null
}