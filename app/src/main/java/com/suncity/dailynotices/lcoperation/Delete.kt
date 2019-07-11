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
        shieldObject.whereEqualTo(TableConstants.SHIELD_ID,AVObject.createWithoutData(TableConstants.TABLE_USER,shieldId))
        shieldObject.getFirstInBackground(object : GetCallback<AVObject>(){

            override fun done(avObject: AVObject?, avException: AVException?) {
                if(avObject != null && avException == null){
                    val sql = "delete from Shield where objectId='${avObject.objectId}'"
                    AVQuery.doCloudQueryInBackground(sql, object : CloudQueryCallback<AVCloudQueryResult>() {

                        override fun done(result: AVCloudQueryResult?, avException: AVException?) {
                            callback(result, avException)
                        }

                    })
                }else{
                    callback(null,avException)
                }
            }

        })

    }
}