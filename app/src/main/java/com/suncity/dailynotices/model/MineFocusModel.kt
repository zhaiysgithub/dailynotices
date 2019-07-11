package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      MineFocusModel
 * @Description:    我查看和查看我的model
 * @UpdateDate:     11/7/2019
 */
class MineFocusModel : Serializable {

    var createDate: Date? = null
    var updateDate: Date? = null
    var userObjcetId: String? = null
    var userName: String? = null
    var userAvatar: String? = null
    var userInfoObjcetId: String? = null
    var userPhoneNum: String? = null
}