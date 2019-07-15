package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback
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


}