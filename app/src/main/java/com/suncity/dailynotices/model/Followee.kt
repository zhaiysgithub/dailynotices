package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Followee
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Followee : Serializable {

    var objectId:String? = null
    var user:User? = null
//    var followee
    var createdAt:Date? = null
    var updatedAt:Date? = null
}