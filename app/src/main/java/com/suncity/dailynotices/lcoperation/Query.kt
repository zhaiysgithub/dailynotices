package com.suncity.dailynotices.lcoperation

import com.alibaba.fastjson.JSONObject
import com.avos.avoscloud.*
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.model.*
import com.suncity.dailynotices.utils.SharedPrefHelper
import com.suncity.dailynotices.utils.StringUtils
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Query
 * @Description:    查询操作
 * @UpdateDate:     10/7/2019
 */
object Query {

    //数量比较多的时候每次请求返回 20 条数据
    private val limite = 20

    /**
     * 推荐我的Fire对象
     */
    fun queryFire(userObjectId: String, callback: ((Fire?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_FIRE)
        query.whereEqualTo(TableConstants.TOUSER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(fire: AVObject?, e: AVException?) {
                if (fire != null && e == null) {
                    SharedPrefHelper.saveAny(Constants.SP_KEY_FIRE, fire)
                    val jsonStr = SharedPrefHelper.retireveAny(Constants.SP_KEY_FIRE)
                    if (StringUtils.isNotEmptyAndNull(jsonStr)) {
                        val gson = Gson()
                        val fireObject = gson.fromJson(jsonStr, Fire::class.javaObjectType)
                        callback(fireObject, null)
                    } else {
                        callback(null, null)
                    }
                } else {
                    callback(null, e)
                }
            }

        })
    }

    /**
     * 查询 匹配的fire 数据的集合
     */
    fun queryFireList(userObjectId: String, callback: ((MutableList<AVObject>?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_FIRE)
        query.whereEqualTo(TableConstants.TOUSER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.findInBackground(object : FindCallback<AVObject>() {

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                callback(avObjects, avException)
            }

        })
    }

    /**
     * 查询 RecentVisit 的信息
     */
    fun queryRecentVisitUser(userObjectId: String, callback: ((Int, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.USER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.countInBackground(object : CountCallback() {
            override fun done(count: Int, e: AVException?) {
                callback(count, e)
            }
        })
    }

    /**
     * 查询 RecentVisit 的集合信息
     * 查询 我查看的
     */
    fun findRecentVisitUserList(userObjectId: String, callback: ((ArrayList<MineFocusModel>?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.USER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val queryList = arrayListOf<AVQuery<AVObject>>()
                    val map = mutableMapOf<String, Date>()
                    avObjects.forEach {

                        val avUser: AVUser = it.getAVUser("visiter")//查询user字段对应的AVUser对象
                        val createAt = it.getDate("createdAt")

                        val objectId = avUser.objectId
                        map[objectId] = createAt
                        val userQuery = AVQuery<AVObject>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, objectId)

                        queryList.add(userQuery)
                    }
                    // OR 组合查询
                    val queryOr = AVQuery.or(queryList)

                    findUserByQueryOr(queryOr, map, callback)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 通过组合 or 查询所有的 user 信息
     */
    fun findUserByQueryOr(
        queryOr: AVQuery<AVObject>,
        map: MutableMap<String, Date>,
        callback: ((ArrayList<MineFocusModel>?, AVException?) -> Unit)
    ) {

        queryOr.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val modelList = arrayListOf<MineFocusModel>()

                    for (item in 0 until avObjects.size) {
                        val mineFocusModel = MineFocusModel()
                        val avObject = avObjects[item]
                        mineFocusModel.userName = avObject.getString("username")

                        val id = avObject.objectId
                        mineFocusModel.userObjcetId = id

                        val createAt = map[id]
                        mineFocusModel.createDate = createAt

                        val avatarFile = avObject.getAVFile<AVFile>("avatar")
                        mineFocusModel.userAvatar = avatarFile.url

                        val infoPointer = avObject.getAVObject<AVObject>("info")
                        mineFocusModel.userInfoObjcetId = infoPointer.objectId

                        mineFocusModel.userPhoneNum = avObject.getString("mobilePhoneNumber")

                        modelList.add(mineFocusModel)
                    }
                    callback(modelList, null)
                } else {
                    callback(null, avException)
                }
            }

        })
    }


    /**
     * 查询 RecentVisit 的信息
     */
    fun queryRecentVisitVisit(userObjectId: String, callback: ((Int, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.VISITER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.countInBackground(object : CountCallback() {
            override fun done(count: Int, e: AVException?) {
                callback(count, e)
            }
        })
    }

    /**
     * 查询 RecentVisit 的集合信息
     */
    fun findRecentVisitVisiterList(
        userObjectId: String,
        callback: ((ArrayList<MineFocusModel>?, AVException?) -> Unit)
    ) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.VISITER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val queryList = arrayListOf<AVQuery<AVObject>>()
                    val map = mutableMapOf<String, Date>()
                    avObjects.forEach {

                        val avUser: AVUser = it.getAVUser("user")//查询user字段对应的AVUser对象
                        val createAt = it.getDate("createdAt")

                        val objectId = avUser.objectId
                        map[objectId] = createAt
                        val userQuery = AVQuery<AVObject>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, objectId)

                        queryList.add(userQuery)
                    }
                    // OR 组合查询
                    val queryOr = AVQuery.or(queryList)

                    findUserByQueryOr(queryOr, map, callback)
                } else {
                    callback(null, avException)
                }
            }

        })
    }


    /**
     * 在 RecentNotice表中 通过 objectId 查询 Notice表中的数据
     */
    fun queryRecentNoticeByObjectId(userObjectId: String, callback: ((ArrayList<Notice>?, AVException?) -> Unit)) {

        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTNOTICE)
        query.whereEqualTo(TableConstants.USER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val queryList = arrayListOf<AVQuery<AVObject>>()
                    avObjects.forEach {

                        val noticePointer: AVObject = it.getAVObject("notice")
                        val noticeQuery = AVQuery<AVObject>(TableConstants.TABLE_NOTICE)
                        noticeQuery.whereEqualTo(TableConstants.OBJECTID, noticePointer.objectId)
                        queryList.add(noticeQuery)
                    }
                    // OR 组合查询
                    val noticeQueryOr = AVQuery.or(queryList)

                    findNoticeByQueryOr(noticeQueryOr, callback)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 组合 OR 查询 notice 表
     */
    private fun findNoticeByQueryOr(
        noticeQueryOr: AVQuery<AVObject>,
        callback: (ArrayList<Notice>?, AVException?) -> Unit
    ) {
        noticeQueryOr.findInBackground(object : FindCallback<AVObject>() {

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val noticeList = arrayListOf<Notice>()
                    val queryList = arrayListOf<AVQuery<AVUser>>()
                    avObjects.forEach {
                        val notice = Notice()
                        notice.title = it.getString("title")
                        notice.endTime = it.getString("endTime")
                        notice.payment = it.getString("payment")
                        notice.contents = it.getString("contents")
                        val userPoint: AVUser = it.getAVUser("user")
                        val userObjectId = userPoint.objectId
                        notice.userId = userObjectId
                        noticeList.add(notice)

                        val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, userObjectId)
                        queryList.add(userQuery)
                    }

                    // OR 组合查询
                    val userQueryOr = AVQuery.or(queryList)

                    findNoticeUserByQueryOr(userQueryOr, noticeList) { list, exception ->
                        if (list != null && list.size > 0 && exception == null) {
                            callback(list, null)
                        } else {
                            callback(noticeList, exception)
                        }
                    }
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 查询 notice 表中user属性
     */
    private fun findNoticeUserByQueryOr(
        userQueryOr: AVQuery<AVUser>,
        noticeList: ArrayList<Notice>,
        callback: (ArrayList<Notice>?, AVException?) -> Unit
    ) {
        userQueryOr.findInBackground(object : FindCallback<AVUser>() {

            override fun done(avObjects: MutableList<AVUser>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {

                    for (item in 0 until avObjects.size) {
                        val avUser = avObjects[item]
                        noticeList.forEach {
                            if (it.userId == avUser.objectId) {
                                it.userName = avUser.username
                            }
                        }

                    }
                    callback(noticeList, null)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     *  查询 shield 的信息
     */
    fun queryShield(userObjectId: String, callback: ((ArrayList<Shield>?, AVException?) -> Unit)) {
        //查询 shield 表
        val queryShield = AVQuery<AVObject>(TableConstants.TABLE_SHIELD)
        queryShield.whereEqualTo(
            TableConstants.USER,
            AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId)
        )
        queryShield.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val queryList = arrayListOf<AVQuery<AVUser>>()
                    avObjects.forEach {
                        val shieldUser = it.getAVUser<AVUser>("shieldId")

                        val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, shieldUser.objectId)
                        queryList.add(userQuery)
                    }
                    // OR 组合查询
                    val userQueryOr = AVQuery.or(queryList)

                    findShieldUserByQueryOr(userQueryOr) { shieldList, exception ->
                        if (shieldList != null && shieldList.size > 0 && exception == null) {
                            callback(shieldList, null)
                        } else {
                            callback(null, exception)
                        }
                    }
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 组合查询 被屏蔽人的信息
     */
    private fun findShieldUserByQueryOr(
        userQueryOr: AVQuery<AVUser>?,
        callback: (ArrayList<Shield>?, AVException?) -> Unit
    ) {

        userQueryOr?.findInBackground(object : FindCallback<AVUser>() {

            override fun done(avObjects: MutableList<AVUser>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val shieldList = arrayListOf<Shield>()
                    val queryList = arrayListOf<AVQuery<AVObject>>()
                    avObjects.forEach {
                        val shield = Shield()
                        shield.objectId = it.objectId
                        shield.shieldUserName = it.username
                        shield.avatarurl = (it.getAVFile<AVFile>("avatar")?.url ?: "")
                        val shieldInfoId = (it.getAVObject<AVObject>("info")?.objectId ?: "")
                        shield.userInfoId = shieldInfoId
                        shieldList.add(shield)

                        val userInfoQuery = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
                        userInfoQuery.whereEqualTo(TableConstants.USER, shieldInfoId)
                        queryList.add(userInfoQuery)
                    }
                    // OR 组合查询
                    val userinfoQueryOr = AVQuery.or(queryList)
                    findShieldUserInfoByQueryOr(userinfoQueryOr, shieldList) { datas, exception ->
                        if (datas != null && datas.size > 0 && exception == null) {
                            callback(datas, null)
                        } else {
                            callback(shieldList, avException)
                        }
                    }
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    private fun findShieldUserInfoByQueryOr(
        userinfoQueryOr: AVQuery<AVObject>?,
        shieldList: ArrayList<Shield>,
        callback: (ArrayList<Shield>?, AVException?) -> Unit
    ) {
        userinfoQueryOr?.findInBackground(object : FindCallback<AVObject>() {

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    avObjects.forEach { avObject ->
                        val userIdStr = avObject.getString("user")
                        shieldList.forEach {
                            if (it.userInfoId == userIdStr) {
                                it.autonym = avObject.getInt("autonym")
                            }
                        }
                    }
                    callback(shieldList, null)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 个人详情页查询个人信息
     */
    fun queryUserByObjectId(objectId: String, callback: ((AVUser?, AVException?) -> Unit)) {
        val avQuery = AVUser.getQuery()
        avQuery.getInBackground(objectId, object : GetCallback<AVUser>() {
            override fun done(avUser: AVUser?, e: AVException?) {
                callback(avUser, e)
            }

        })
    }

    /**
     * 首页通告栏
     */
    fun queryAllRecentNotice(callback: ((ArrayList<Notice>?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_NOTICE)

        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val noticeList = arrayListOf<Notice>()
                    val queryList = arrayListOf<AVQuery<AVUser>>()
                    avObjects.forEach {
                        val notice = Notice()
                        notice.title = it.getString("title")
                        notice.endTime = it.getString("endTime")
                        notice.payment = it.getString("payment")
                        notice.contents = it.getString("contents")
                        val userPoint: AVUser = it.getAVUser("user")
                        val userObjectId = userPoint.objectId
                        notice.userId = userObjectId
                        noticeList.add(notice)

                        val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, userObjectId)
                        queryList.add(userQuery)

                    }
                    // OR 组合查询
                    val userQueryOr = AVQuery.or(queryList)

                    findNoticeUserByQueryOr(userQueryOr, noticeList) { list, exception ->
                        if (list != null && list.size > 0 && exception == null) {
                            callback(list, null)
                        } else {
                            callback(noticeList, exception)
                        }
                    }
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 红人页面数据查询
     */
    fun queryAllCover(callback: ((ArrayList<Cover>?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_COVER)
        query.findInBackground(object : FindCallback<AVObject>() {

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val coverList = arrayListOf<Cover>()
                    val queryUserList = arrayListOf<AVQuery<AVUser>>()
                    val queryUserInfoList = arrayListOf<AVQuery<AVObject>>()
                    avObjects.forEach {
                        val itemCover = Cover()
                        val coverFile = it.getAVFile<AVFile>("cover")
                        itemCover.objectId = it.objectId
                        itemCover.coverUrl = coverFile.url
                        itemCover.createdAt = it.getDate("createdAt")
                        itemCover.updateAt = it.getDate("updateAt")
                        val userInfoPointer = it.getAVObject<AVObject>("user")
                        val idPointer = userInfoPointer.objectId
                        itemCover.userObjectId = idPointer
                        coverList.add(itemCover)
                        //查询user表中的userName
                        val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
                        userQuery.whereEqualTo(TableConstants.OBJECTID, idPointer)
                        queryUserList.add(userQuery)

                        //查询userInfo表中的fire字段

                        val userInfoQuery = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
                        userInfoQuery.whereEqualTo(TableConstants.USER, idPointer)
                        queryUserInfoList.add(userInfoQuery)
                    }
                    // OR 组合查询
                    val userQueryOr = AVQuery.or(queryUserList)
                    val userInfoQueryOr = AVQuery.or(queryUserInfoList)
                    findUserInfoByQueryOr(userQueryOr, userInfoQueryOr, coverList) { covers, e ->
                        callback(covers, e)
                    }
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 组合查询 user 和 userInfo
     */
    private fun findUserInfoByQueryOr(
        userQueryOr: AVQuery<AVUser>?,
        userInfoQueryOr: AVQuery<AVObject>?,
        coverList: ArrayList<Cover>,
        callback: (ArrayList<Cover>?, AVException?) -> Unit
    ) {
        userQueryOr?.findInBackground(object : FindCallback<AVUser>() {
            override fun done(avUsers: MutableList<AVUser>?, avException: AVException?) {

                userInfoQueryOr?.findInBackground(object : FindCallback<AVObject>() {

                    override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                        setCoverData(coverList, avUsers, avObjects, avException, callback)
                    }

                })
            }
        })


    }

    /**
     * 组合查询 user 和 userInfo 的内容返回给cover集合
     */
    private fun setCoverData(
        coverList: ArrayList<Cover>,
        avUsers: MutableList<AVUser>?,
        avUserInfos: MutableList<AVObject>?,
        avException: AVException?,
        callback: (ArrayList<Cover>?, AVException?) -> Unit
    ) {
        coverList.forEach { cover ->
            val user = avUsers?.find { cover.userObjectId == it.objectId }
            val userInfo = avUserInfos?.find { cover.userObjectId == it.getString("user") }
            cover.userName = user?.username
            cover.fireCount = userInfo?.getInt("fire")
        }
        callback(coverList, avException)
    }


    /**
     * 1:未登录查询所有的数据
     * 2:已经登录的了，需要过滤 shield 的数据
     * 3:关联的表有 Dynamic , user , shield ,userInfo,like
     */
    fun queryDynamicData(
        isLogin: Boolean,
        userObjectId: String,
        callback: (ArrayList<Dynamic>?, AVException?) -> Unit
    ) {

        if (isLogin) {
            // 查询 shield 表中的过滤数据
            val shieldQuery = AVQuery<AVObject>(TableConstants.TABLE_SHIELD)

            shieldQuery.whereEqualTo(
                TableConstants.USER,
                AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId)
            )

            // 查询 Like 表中的过滤数据
            val likeQuery = AVQuery<AVObject>(TableConstants.TABLE_LIKE)
            likeQuery.whereEqualTo(
                TableConstants.USER,
                AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId)
            )

            shieldQuery.findInBackground(object : FindCallback<AVObject>() {
                override fun done(avShields: MutableList<AVObject>?, avException: AVException?) {
                    // shield 表查询异常不影响 dynamic 的数据查询
                    // 查询 Like 表
                    queryLikeAll(likeQuery) {
                        //点过赞的id集合
                        val likeIdList = arrayListOf<String>()
                        it?.forEach { like ->
                            val likeId = like.getString("likedId")
                            likeIdList.add(likeId)
                        }
                        //屏蔽过的id集合
                        val shieldIdList = arrayListOf<String>()
                        avShields?.forEach {shield ->
                            val avObject = shield.getAVObject<AVObject>("shieldId")
                            val idPointer = avObject.objectId
                            shieldIdList.add(idPointer)
                        }

                        queryDynamicAll(likeIdList,shieldIdList, callback)
                    }

                }
            })
        } else {
            queryDynamicAll(null, null, callback)
        }
    }

    /**
     * 查询like数据
     */
    fun queryLikeAll(likeQuery: AVQuery<AVObject>, callback: ((MutableList<AVObject>?) -> Unit)) {

        likeQuery.findInBackground(object : FindCallback<AVObject>() {
            override fun done(likes: MutableList<AVObject>?, avException: AVException?) {
                callback(likes)
            }
        })
    }

    /**
     * 查询 首页动态数据
     */
    private fun queryDynamicAll(
        likeIdList: ArrayList<String>?,
        shieldIdList: ArrayList<String>?,
        callback: (ArrayList<Dynamic>?, AVException?) -> Unit
    ) {
        val dynamicQuery = AVQuery<AVObject>(TableConstants.TABLE_DYNAMIC)
        dynamicQuery.findInBackground(object : FindCallback<AVObject>() {

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avException == null && avObjects != null && avObjects.size > 0) {
                    val dynamicList = arrayListOf<Dynamic>()
                    if ((shieldIdList?.size == 0)) {
                        setDynamicData(dynamicList, avObjects,likeIdList) {
                            callback(it, null)
                        }
                    } else {
                        //过滤 shield 后的集合
                        if(shieldIdList != null){
                            val filterList = avObjects.filterTo(arrayListOf()) {
                                val avUser = it.getAVObject<AVObject>("user")
                                !shieldIdList.contains(avUser.objectId)
                            }
                            setDynamicData(dynamicList, filterList,likeIdList) {
                                callback(it, null)
                            }
                        }else{
                            setDynamicData(dynamicList, avObjects,likeIdList) {
                                callback(it, null)
                            }
                        }
                    }

                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 填充数据
     */
    private fun setDynamicData(
        dynamicList: ArrayList<Dynamic>,
        avDynamics: MutableList<AVObject>,
        likeIdList: ArrayList<String>?,
        callback: (ArrayList<Dynamic>) -> Unit
    ) {
        try{
            val avUserQueryList = arrayListOf<AVQuery<AVUser>>()
            val avUserInfoQueryList = arrayListOf<AVQuery<AVObject>>()
            avDynamics.forEach {
                val dynamic = Dynamic()
                val id = it.objectId
                dynamic.objectId = id
                dynamic.createAt = it.createdAt
                dynamic.updateAt = it.updatedAt
                dynamic.contents = it.getString("contents")
                val jsonArray = it.getJSONArray("images")
                val arrList: MutableList<String> = JSONObject.parseArray(jsonArray.toString(), String::class.javaObjectType)
                dynamic.images = arrList
                val skill = it.getString("skill")
                dynamic.skill = skill
                dynamic.likeNum = it.getInt("likeNum")
                dynamic.able = it.getInt("able")
                val style = it.getString("style")
                dynamic.style = style
                dynamic.fire = it.getInt("fire")
                val tags = arrayListOf<String>()
                if (StringUtils.isNotEmptyAndNull(skill)) {
                    tags.add("# $skill")
                }
                if (StringUtils.isNotEmptyAndNull(style)) {
                    tags.add("# $style")
                }
                dynamic.tagList = tags
                //是否点过赞了
                dynamic.isSelected = (likeIdList?.contains(id) ?: false)
                //user 表
                val avUser = it.getAVUser<AVUser>("user")
                val userId = avUser.objectId
                dynamic.idPointer = userId
                dynamicList.add(dynamic)

                //or 组合查询
                val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
                userQuery.whereEqualTo(TableConstants.OBJECTID, userId)
                avUserQueryList.add(userQuery)

                val userInfoQuery = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
                userInfoQuery.whereEqualTo(TableConstants.OBJECTID,userId)
                avUserInfoQueryList.add(userInfoQuery)


            }
            val queryUserByOr = AVQuery.or(avUserQueryList)
            queryUserByOr.findInBackground(object : FindCallback<AVUser>(){

                override fun done(avUsers: MutableList<AVUser>?, avException: AVException?) {
                    avUsers?.forEach { avUser ->
                        dynamicList.forEach {
                            if(it.idPointer == avUser.objectId){
                                it.userName = avUser.username
                                it.avatarurl = avUser.getAVFile<AVFile>("avatar").url
                            }
                        }
                    }

                    val queryUserInfoByOr = AVQuery.or(avUserInfoQueryList)
                    queryUserInfoByOr.findInBackground(object : FindCallback<AVObject>(){
                        override fun done(avUserInfos: MutableList<AVObject>?, avException: AVException?) {
                            avUserInfos?.forEach { avUserInfo ->
                                dynamicList.forEach {
                                    if(it.idPointer == avUserInfo.objectId){
                                        it.userAutonym = avUserInfo.getInt("autonym")
                                    }
                                }
                            }
                            callback(dynamicList)
                        }

                    })
                }
            })
        }catch (e : Exception){
            callback(dynamicList)
        }

    }
}

