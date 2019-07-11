package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.model.Fire
import com.suncity.dailynotices.model.MineFocusModel
import com.suncity.dailynotices.utils.SharedPrefHelper
import com.suncity.dailynotices.utils.StringUtils
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
                    val map = mutableMapOf<String,Date>()
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

                    findUserByQueryOr(queryOr,map,callback)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

    /**
     * 通过组合 or 查询所有的 user 信息
     */
    fun findUserByQueryOr(queryOr: AVQuery<AVObject>, map: MutableMap<String, Date>,callback: ((ArrayList<MineFocusModel>?, AVException?) -> Unit)) {

        queryOr.findInBackground(object : FindCallback<AVObject>(){
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if(avObjects?.size != map.size){
                    callback(null,avException)
                    return
                }
                if (avObjects.size > 0 && avException == null){
                    val modelList = arrayListOf<MineFocusModel>()

                    for(item in 0 until avObjects.size){
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
                    callback(modelList,null)
                }else{
                    callback(null,avException)
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
    fun findRecentVisitVisiterList(userObjectId: String, callback: ((ArrayList<MineFocusModel>?, AVException?) -> Unit)) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.VISITER, AVObject.createWithoutData(TableConstants.TABLE_USER, userObjectId))
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                if (avObjects != null && avObjects.size > 0 && avException == null) {
                    val queryList = arrayListOf<AVQuery<AVObject>>()
                    val map = mutableMapOf<String,Date>()
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

                    findUserByQueryOr(queryOr,map,callback)
                } else {
                    callback(null, avException)
                }
            }

        })
    }

}