package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Shield
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Shield : Serializable {

    var objectId:String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var avatarurl:String? = null
    var shieldUserName:String? = null
    var userCategory:String? = null
    var userInfoId:String? = null
    var autonym:Int? = null
}