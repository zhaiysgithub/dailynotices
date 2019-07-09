package com.suncity.dailynotices.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      Installation
 * @Description:     作用描述
 * @UpdateDate:     9/7/2019
 */

class Installation : Serializable {

    var objectId:String? = null
    var valid:Boolean? = null
    var vendor:String? = null
    var timeZone:String? = null
    var channels:ArrayList<String>? = null
    var apnsTeamId:String? = null
    var deviceToken:String? = null
    var deviceType:String? = null
    var installationId:String? = null
    var badge:Int? = null
    var apnsTopic:String? = null
    var createdAt: Date? = null
    var updatedAt:Date? = null
}