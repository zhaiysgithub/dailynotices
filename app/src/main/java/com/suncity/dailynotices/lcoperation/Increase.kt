package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.StringUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Increase
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */

object Increase {

    /**
     * 保存userInfo 到后台返回userinfoId
     */
    fun createUserInfoToBack(objectId: String, callback: ((String?, AVException?) -> Unit)) {

        val userInfo = AVObject(TableConstants.TABLE_USERINFO)
        userInfo.put("user", objectId)
        userInfo.put("resume", "")
        userInfo.put("autonym", 0)
        userInfo.put("fire", 0)
        userInfo.put("region", "")
        userInfo.put("age", "")
        userInfo.put("sex", "1")
        userInfo.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                Query.queryUserInfoObjectId(objectId) { userInfo, avException ->
                    val userInfoId = userInfo?.objectId
                    callback(userInfoId, avException)
                }
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
    fun createLike(likeId: String) {
        val userObjectId = PreferenceStorage.userObjectId
        val likeObject = AVObject(TableConstants.TABLE_LIKE)
        likeObject.put("likedId", likeId)
        likeObject.put("user", AVUser.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        likeObject.saveInBackground()
    }


    fun createComment(dynamicId: String, commentContents: String, callback: (AVException?) -> Unit) {
        val userObjectId = PreferenceStorage.userObjectId
        val commentObject = AVObject(TableConstants.TABLE_COMMENTS)
        commentObject.put("user", AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        commentObject.put("dynamicId", AVObject.createWithoutData(TableConstants.TABLE_DYNAMIC, dynamicId))
        commentObject.put("comments", commentContents)
        commentObject.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }


    fun addFeedback(userObjectId: String, content: String, callback: (AVException?) -> Unit) {
        val feedbackObject = AVObject(TableConstants.TABLE_FEEDBACK)
        feedbackObject.put("feedBack", content)
        feedbackObject.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }
        })
    }

    /**
     * 批量上传文件
     */
    fun uploadAVFile(filelocalPaths: ArrayList<String>, callback: (Boolean, ArrayList<String>, AVException?) -> Unit) {
        val avFileUrls = arrayListOf<String>()
        var index = 0
        val filterResult = filelocalPaths.filter {
            StringUtils.isNotEmptyAndNull(it)
        }
        if (filterResult.isEmpty()) {
            callback(true, avFileUrls, null)
            return
        }
        val valueSize = filterResult.size
        filterResult.forEach {
            val avFile = AVFile.withAbsoluteLocalPath(StringUtils.getRandomFileName(20), it)
            avFile.saveInBackground(object : SaveCallback() {
                override fun done(e: AVException?) {
                    index++
                    if (e == null) {
                        avFileUrls.add(avFile.url)
                        if (index == valueSize) {
                            checkUploadFinished(index, avFileUrls, callback)
                        }
                    } else {
                        callback(false, arrayListOf(), e)
                    }

                }
            })
        }

    }

    /**
     * 检测是否上传完成
     */
    private fun checkUploadFinished(
        index: Int,
        urls: ArrayList<String>,
        callback: (Boolean, ArrayList<String>, AVException?) -> Unit
    ) {
        if (index == urls.size) {
            callback(true, urls, null)
        } else {
            callback(false, urls, null)
        }
    }

    /**
     * 上传单个图片返回avFile对象
     */
    fun uploadLocalPicAVFile(localPath: String, callback: (AVFile?, AVException?) -> Unit) {
        if (StringUtils.isEmptyOrNull(localPath)) callback(null, null)
        val avFile = AVFile.withAbsoluteLocalPath(StringUtils.getRandomPicName(10), localPath)
        avFile.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(avFile, e)
            }

        })
    }

    /**
     * 上传动态内容
     */
    fun uploadDynamicData(
        avFileUrls: ArrayList<String>, desc: String?, skillContent: String?, styleContent: String?
        , isVideo: Boolean, videoThumbnail: String?
        , callback: (AVException?) -> Unit
    ) {
        val userId = PreferenceStorage.userObjectId
        val dynamicObject = AVObject(TableConstants.TABLE_DYNAMIC)
        dynamicObject.put("images", avFileUrls)
        dynamicObject.put("style", styleContent)
        dynamicObject.put("skill", skillContent)
        dynamicObject.put("likeNum", 0)
        dynamicObject.put("contents", desc)
        dynamicObject.put("able", 2)
        dynamicObject.put("isVideo", if (isVideo) 1 else 0)
        if (isVideo && videoThumbnail != null && videoThumbnail.isNotEmpty()) {
            dynamicObject.put("videoImage", videoThumbnail)
        }
        dynamicObject.put("user", AVObject.createWithoutData(TableConstants.TABLE_USER, userId))
        dynamicObject.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }
        })
    }

    /**
     * 被访问者的id
     * 我查看的
     * 我的id -> user
     * 对方的id -> visiter
     */
    fun createRecentVister(vistedObjectId: String) {
        val userId = PreferenceStorage.userObjectId
        Query.queryRecentVisitUser(userId) { avObjects ->
            val containsIdList = mutableListOf<String>()
            if (avObjects != null && avObjects.size > 0) {
                avObjects.forEach {
                    val visiterId = it.getAVUser<AVUser>("visiter")?.objectId ?: ""
                    containsIdList.add(visiterId)
                }
            }
            if (!containsIdList.contains(vistedObjectId)) {
                val recentVisitObject = AVObject(TableConstants.TABLE_RECENTVISIT)
                recentVisitObject.put("user", AVUser.createWithoutData(TableConstants.TABLE_USER, userId))
                recentVisitObject.put("visiter", AVUser.createWithoutData(TableConstants.TABLE_USER, vistedObjectId))
                recentVisitObject.saveInBackground(object : SaveCallback() {
                    override fun done(e: AVException?) {
                        if (e == null) {
                            GlobalObserverHelper.onNotifyRecentVisitUser()
                        }
                    }
                })
            }
        }


    }

}