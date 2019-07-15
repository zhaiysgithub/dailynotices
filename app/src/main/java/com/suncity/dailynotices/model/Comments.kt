package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Comments
 * @Description:    评论
 * @UpdateDate:     9/7/2019
 */
class Comments : Serializable {

    var objectId: String? = null
    var comments: String? = null
    var userPointerId: String? = null
    var dynamicPointerId: String? = null
    var createAt: Date? = null
    var updateAt: Date? = null
    var userAvatar: String? = null
    var userName: String? = null
    var userInfoPointerId: String? = null
}