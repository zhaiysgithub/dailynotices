package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Dynamic
 * @Description:    动态需要的字段
 * @UpdateDate:     9/7/2019
 */
class Dynamic : Serializable {

    // dynaimc 表中的
    var objectId: String? = null
    var createAt: Date? = null
    var updateAt: Date? = null
    var contents:String? = null
    var images:MutableList<String>? = null
    var skill: String? = null
    var likeNum: Int? = null
    var able: Int? = null
    var style: String? = null
    var fire: Int? = null
    var tagList:ArrayList<String>? = null
    // user 表中的
    var idPointer: String? = null
    var avatarurl: String? = null
    var userName: String? = null
    //userInfo
    var userAutonym : Int? = null
    //like表  是否点过赞了
    var isSelected : Boolean = false

}