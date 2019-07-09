package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Dynamic
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class Dynamic : Serializable {

    var objectId: String? = null
    var fire: Int? = null
    var skill: String? = null
    var images: ArrayList<String>? = null
    var style: String? = null
    var likeNum: Int? = null
    var contents: String? = null
    var able: Int? = null
    var user: User? = null
    var createdAt: Date? = null
    var updateAt: Date? = null

}