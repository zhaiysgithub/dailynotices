package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Notice
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Notice : Serializable {

    var objectId: String? = null
    var workTime: String? = null
    var payment: String? = null
    var endTime: String? = null
    var title: String? = null
    var contents: String? = null
    var image: String? = null
    var workPlace: String? = null
    var beginTime: String? = null
    var userId: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var userName: String? = null
}