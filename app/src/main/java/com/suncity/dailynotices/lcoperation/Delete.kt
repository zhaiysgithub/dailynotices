package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.suncity.dailynotices.TableConstants

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Delete
 * @Description:    数据库删除操作
 * @UpdateDate:     10/7/2019
 */

object Delete {

    fun deleteShieldById(shieldId: String, callback: ((AVCloudQueryResult?, AVException?) -> Unit)) {

        val shieldObject = AVQuery<AVObject>(TableConstants.TABLE_SHIELD)
        shieldObject.whereEqualTo(
            TableConstants.SHIELD_ID,
            AVObject.createWithoutData(TableConstants.TABLE_USER, shieldId)
        )
        shieldObject.getFirstInBackground(object : GetCallback<AVObject>() {

            override fun done(avObject: AVObject?, avException: AVException?) {
                if (avObject != null && avException == null) {
                    val sql = "delete from Shield where objectId='${avObject.objectId}'"
                    AVQuery.doCloudQueryInBackground(sql, object : CloudQueryCallback<AVCloudQueryResult>() {

                        override fun done(result: AVCloudQueryResult?, avException: AVException?) {
                            callback(result, avException)
                        }

                    })
                } else {
                    callback(null, avException)
                }
            }

        })

    }

    /**
     * 通過 id 刪除帖子
     */
    fun delPostById(dynamicId: String, callback: ((AVException?) -> Unit)) {

        val dynamicObject = AVQuery<AVObject>(TableConstants.TABLE_DYNAMIC)
        dynamicObject.whereEqualTo(TableConstants.OBJECTID, dynamicId)
        dynamicObject.deleteAllInBackground(object : DeleteCallback() {

            override fun done(exception: AVException?) {
                if (exception == null) {
                    callback(null)
                } else {
                    callback(exception)
                }
            }

        })
    }
}