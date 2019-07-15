package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreferenceStorage

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Increase
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */

object Increase {

    fun createUserInfoToBack(objectId: String, callback: ((AVException?) -> Unit)) {

        val userInfo = AVObject(TableConstants.TABLE_USERINFO)
        userInfo.put("user", objectId)
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


    fun createShieldToBack(objectId: String, callback: ((AVException?) -> Unit)) {
        val shie = AVObject(TableConstants.TABLE_SHIELD)
        shie.put("user", AVObject.createWithoutData(TableConstants.TABLE_USER, PreferenceStorage.userObjectId))
        shie.put("shieldId", AVObject.createWithoutData(TableConstants.TABLE_USER, objectId))
        shie.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                LogUtils.e(e?.toString() ?: "")
                callback(e)
            }

        })
    }

    /**
     * 更新 Like 表
     */
    fun createLike(likeId:String){
        val userObjectId = PreferenceStorage.userObjectId
        val likeObject = AVObject(TableConstants.TABLE_LIKE)
        likeObject.put("likedId",likeId)
        likeObject.put("user",AVUser.createWithoutData(TableConstants.TABLE_USER,userObjectId))
        likeObject.saveInBackground()
    }
}