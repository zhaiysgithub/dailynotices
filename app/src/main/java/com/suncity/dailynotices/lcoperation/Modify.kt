package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.suncity.dailynotices.TableConstants


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Modify
 * @Description:    数据库修改操作
 * @UpdateDate:     10/7/2019
 */

object Modify {

    /**
     * 更新 likeNum
     */
    fun updateDynamicLikeNum(objectId: String, originNum: Int, callback: (AVException?) -> Unit) {
        Increase.createLike(objectId)
        val dynamic = AVObject.createWithoutData(TableConstants.TABLE_DYNAMIC, objectId)
        dynamic.put("likeNum", originNum + 1)
        dynamic.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }


    /**
     * 更新user表中的userInfo字段
     */
    fun updateUserToBack(userObjectId: String, userInfoId: String,callback: (AVException?) -> Unit) {
        val user = AVUser.createWithoutData(TableConstants.TABLE_USER, userObjectId)
        user.put("info",AVObject.createWithoutData(TableConstants.TABLE_USERINFO,userInfoId))
        user.saveInBackground(object : SaveCallback(){
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }


}