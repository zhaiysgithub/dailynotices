package com.suncity.dailynotices.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      FeedBack
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class FeedBack : Serializable {

    var objectId:String? = null
    var feedBack:String? = null
    var createdAt:String? = null
    var updatedAt:String? = null
}