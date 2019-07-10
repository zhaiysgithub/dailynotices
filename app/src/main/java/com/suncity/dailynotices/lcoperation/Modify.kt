package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.AVObject
import com.suncity.dailynotices.TableConstants

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Modify
 * @Description:    数据库修改操作
 * @UpdateDate:     10/7/2019
 */

object Modify {

    fun updateUserPhone(user:AVObject,newPhoneNum: String, objectId: String) {

        user.increment(TableConstants.USER_MOBILEPHONENUMBER,1)

    }
}