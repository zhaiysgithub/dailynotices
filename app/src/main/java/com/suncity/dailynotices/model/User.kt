package com.suncity.dailynotices.model

import java.io.Serializable


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      User
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */

class User : Serializable{

    var username:String? = null
    var emailVerified:Boolean? = null
    var mobilePhoneNumber:String? = null
    var avatar:AvFile? = null
    var info:UserInfo? = null
    var mobilePhoneVerified:Boolean? = null
    var objectId:String? = null
    var salt:String? = null
    var email:String? = null
    var sessionToken:String? = null
    var createdAt:String? = null
    var updatedAt:String? = null


}