package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Cover
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Cover : Serializable {

    var objectId:String? = null
    var cover:AvFile? = null
    var className:String? = null
    var user:User? = null
    var createdAt: Date? = null
    var updateAt:Date? = null
}