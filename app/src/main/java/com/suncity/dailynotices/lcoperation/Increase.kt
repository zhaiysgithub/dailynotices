package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback
import com.suncity.dailynotices.TableConstants

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Increase
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */

object Increase{

    fun createUserInfoToBack(objectId:String,callback:((AVException?) -> Unit)){

        val userInfo = AVObject(TableConstants.TABLE_USERINFO)
        userInfo.put("user",objectId)
        userInfo.put("resume", "")
        userInfo.put("autonym", 0)
        userInfo.put("fire", 0)
        userInfo.put("region", "")
        userInfo.put("age", "")
        userInfo.put("sex", "0")
        userInfo.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }
        })
    }
}