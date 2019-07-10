package com.suncity.dailynotices.localModel


import java.io.Serializable

/**
 * @ProjectName: dailynotices
 * @Package: com.suncity.dailynotices.LocalModel
 * @ClassName: LCUser
 * @Description: 作用描述
 * @UpdateDate: 9/7/2019
 */
class LCUser : Serializable {

    var emailVerified: Boolean = false
    var mobilePhoneNumber: String? = null
    var sessionToken: String? = null
    var avatar: LCAvatar? = null
    var mobilePhoneVerified: Boolean = false
    var username: String? = null
    var info: LCInfo? = null


}
