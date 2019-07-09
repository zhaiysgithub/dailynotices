package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      TestObject
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */
class TestObject : Serializable {

    var objectId:String? = null
    var words:String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
}