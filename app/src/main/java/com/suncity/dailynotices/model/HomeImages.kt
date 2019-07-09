package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      HomeImages
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class HomeImages : Serializable {

    var objectId: String? = null
    var image: AvFile? = null
    var user: User? = null
    var userName: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
}