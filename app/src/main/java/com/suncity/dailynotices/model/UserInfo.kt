package com.suncity.dailynotices.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      UserInfo
 * @Description:    dataBean
 * @UpdateDate:     8/7/2019
 */
class UserInfo : Serializable {

    var user: String? = null
    var sex: String? = null
    var birthday: String? = null
    var region: String? = null
    var objectId: String? = null
    var fire: Int? = null
    var autonym: Int? = null
    var intimityInfo: IntimityInfo? = null
}