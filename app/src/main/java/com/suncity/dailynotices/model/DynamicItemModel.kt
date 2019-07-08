package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      DynamicItemModel
 * @Description:     作用描述
 * @UpdateDate:     8/7/2019
 */
class DynamicItemModel : Serializable{

    var user: String? = null
    var userName: String? = null
    var contents: String? = null
    var style: String? = null
    var skill: String? = null
    var createdAt: Date? = null
    /*var avatar: LCFile? = LCFile()
    var images: [String] = []
    var objectId: String = ""
    var likeNum: Int = 0
    var likeUsers: [LCObject] = [] //点赞列表
    var fire: Int = 0
    var isLiked: Bool = false
    var likedId: String = "" //自己点赞索引*/
}