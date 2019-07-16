package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      UserInfo
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class UserInfo :Serializable{

    var objectId:String? = null
    var fire:Int? = null
    var shoeSize:String? = null
    var bwh:String? = null
    var graduation:String? = null
    var living:String? = null
    var age:String? = null
    var sex:String? = null
    var nationality:String? = null
    var birthday:String? = null
    var region:String? = null
    var native:String? = null
    var weight:String? = null
    var interest:ArrayList<String>? = null
    var resume:String? = null
    var autonym:Int? = null
    var user:String? = null
    var height:String? = null
    var nation:String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null


}