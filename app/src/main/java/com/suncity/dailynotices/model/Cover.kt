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
    var coverUrl:String? = null
    var userName:String? = null
    var userObjectId:String? = null
    var createdAt: Date? = null
    var updateAt:Date? = null
    var fireCount:Int? = null //userInfo 表中的内容
}