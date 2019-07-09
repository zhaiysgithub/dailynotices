package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Autonym
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Autonym : Serializable {

    var objectId:String? = null
    var idImage:AvFile? = null
    var idNum:String? = null
    var realName:String? = null
    var user:User? = null
    var createdAt:Date? = null
    var updatedAt:Date? = null
}