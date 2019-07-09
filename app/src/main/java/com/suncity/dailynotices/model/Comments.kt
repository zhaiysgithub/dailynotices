package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Comments
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Comments : Serializable {

    var objectId:String? = null
    var comments:String? = null
    var replyId:String? = null
    var user:User? = null
    var dynamicId:Dynamic? = null
    var createAt: Date? = null
    var updateAt:Date? = null
}