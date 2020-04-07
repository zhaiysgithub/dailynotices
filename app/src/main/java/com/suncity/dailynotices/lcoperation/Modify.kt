package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.StringUtils


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
    fun updateUserToBack(userObjectId: String, userInfoId: String, callback: (AVException?) -> Unit) {
        val user = AVUser.createWithoutData(TableConstants.TABLE_USER, userObjectId)
        user.put("info", AVObject.createWithoutData(TableConstants.TABLE_USERINFO, userInfoId))
        user.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }

    /**
     *  更新userInfo表中的认证信息
     */
    fun updateUserInfoAutonym(autonym: Int, callback: (Boolean, AVException?) -> Unit) {
        val userObjectId = PreferenceStorage.userObjectId
        val userInfoObject = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
        userInfoObject.whereEqualTo("user", userObjectId)
        userInfoObject.getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(userInfo: AVObject?, e: AVException?) {
                if (e != null || userInfo == null) {
                    callback(false, e)
                } else {
                    userInfo.put("autonym", autonym)
                    userInfo.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null) {
                                callback(true, e)
                            } else {
                                callback(false, e)
                            }
                        }

                    })
                }
            }
        })
    }

    /**
     * 更新userInfo 表中的interest內容
     */
    fun updateUserInfoInterest(interestArray: ArrayList<String>, callback: (AVException?) -> Unit) {
        val objectId = PreferenceStorage.userObjectId
        val query = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
        query.whereEqualTo("user", objectId)
        query.getFirstInBackground(object : GetCallback<AVObject>() {

            override fun done(userInfo: AVObject?, e: AVException?) {
                if (userInfo != null && e == null) {
                    userInfo.put("interest", interestArray)
                    userInfo.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            callback(e)
                        }

                    })
                } else {
                    callback(e)
                }
            }

        })
    }

    /**
     * 修改用户的背景照
     * @param coverPath 本地的照片的路径
     */
    fun updateCoverFile(objectId: String, coverPath: String, callback: (Boolean, AVException?) -> Unit) {
        Increase.uploadLocalPicAVFile(coverPath) { avFile, e ->
            if (e == null && avFile != null) {
                val coverQuery = AVQuery<AVObject>(TableConstants.TABLE_COVER)
                coverQuery.whereEqualTo("user", AVObject.createWithoutData(TableConstants.TABLE_USER, objectId))
                coverQuery.getFirstInBackground(object : GetCallback<AVObject>() {
                    override fun done(cover: AVObject?, e: AVException?) {
                        if (e != null) {
                            callback(false, e)
                        } else {
                            if (cover != null) {
                                //更新
                                cover.put("cover", avFile.url)
                                cover.saveInBackground(object : SaveCallback() {
                                    override fun done(e: AVException?) {
                                        callback(e == null, e)
                                    }
                                })
                            } else {
                                //新建
                                val newCover = AVObject(TableConstants.TABLE_COVER)
                                newCover.put("cover", avFile)
                                newCover.put("user", AVUser.createWithoutData(TableConstants.TABLE_USER, objectId))
                                newCover.saveInBackground(object : SaveCallback() {
                                    override fun done(e: AVException?) {
                                        callback(e == null, e)
                                    }
                                })
                            }
                        }
                    }

                })
            } else {
                callback(false, e)
            }

        }
    }

    /**
     * 更新用户头像
     */
    fun updateAvatarFile(objectId: String, avatarPath: String, callback: (Boolean, AVException?) -> Unit) {
        Increase.uploadLocalPicAVFile(avatarPath) { avFile, e ->
            if (e == null && avFile != null) {
                val userQuery = AVUser.getQuery()
                userQuery.getInBackground(objectId, object : GetCallback<AVUser>() {
                    override fun done(avUser: AVUser?, e: AVException?) {
                        if (avUser == null) {
                            callback(false, e)
                        } else {
                            avUser.put("avatar", avFile)
                            avUser.saveInBackground(object : SaveCallback() {
                                override fun done(e: AVException?) {
                                    callback(e == null, e)
                                }
                            })
                        }
                    }
                })
            } else {
                callback(false, e)
            }
        }
    }

    /**
     * 批量上传图片到 HomeImage 表中
     */
    fun updateHomeImageList(
        objectId: String,
        picLocalPaths: ArrayList<String>,
        callback: (ArrayList<String>, AVException?) -> Unit
    ) {
        val homeImageList = mutableListOf<AVObject>()
        var count = 0
        val size = picLocalPaths.size
        val urlList = arrayListOf<String>()
        picLocalPaths.forEach {
            val avFile = AVFile.withAbsoluteLocalPath(StringUtils.getRandomPicName(10), it)
            avFile.saveInBackground(object : SaveCallback() {

                override fun done(e: AVException?) {
                    count++
                    if (e == null) {
                        val avHomeImage = AVObject(TableConstants.TABLE_HOMEIMAGES)
                        avHomeImage.put("image", avFile)
                        avHomeImage.put("user", AVUser.createWithoutData(TableConstants.TABLE_USER, objectId))
                        homeImageList.add(avHomeImage)
                        urlList.add(avFile.url)
                    }
                    if (count == size) {
                        uploadImageBatch(homeImageList) { e ->
                            callback(urlList, e)
                        }
                    }
                }

            })
        }

    }

    /**
     * 批量上传图片到 HomeImage 表中
     */
    fun uploadImageBatch(homeImageList: MutableList<AVObject>, callback: (AVException?) -> Unit) {
        AVObject.saveAllInBackground(homeImageList, object : SaveCallback() {
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }


}