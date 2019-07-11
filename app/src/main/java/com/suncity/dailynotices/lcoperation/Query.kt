package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.model.Fire
import com.suncity.dailynotices.utils.SharedPrefHelper
import com.suncity.dailynotices.utils.StringUtils


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      Query
 * @Description:    查询操作
 * @UpdateDate:     10/7/2019
 */
object Query {

    /**
     * 推荐我的Fire对象
     */
    fun queryFire(userObjectId:String,callback:((Fire?,AVException?) -> Unit)){
        val query = AVQuery<AVObject>(TableConstants.TABLE_FIRE)
        query.whereEqualTo(TableConstants.TOUSER,AVObject.createWithoutData(TableConstants.TABLE_USER,userObjectId))
        query.getFirstInBackground(object : GetCallback<AVObject>(){
            override fun done(fire: AVObject?, e: AVException?) {
                if(fire != null && e == null){
                    SharedPrefHelper.saveAny(Constants.SP_KEY_FIRE,fire)
                    val jsonStr = SharedPrefHelper.retireveAny(Constants.SP_KEY_FIRE)
                    if(StringUtils.isNotEmptyAndNull(jsonStr)){
                        val gson = Gson()
                        val fireObject = gson.fromJson(jsonStr,Fire::class.javaObjectType)
                        callback(fireObject,null)
                    }else{
                        callback(null,null)
                    }
                }else{
                    callback(null,e)
                }
            }

        })
    }

    /**
     * 查询 匹配的fire 数据的集合
     */
    fun queryFireList(userObjectId:String,callback:((MutableList<AVObject>?,AVException?) -> Unit)){
        val query = AVQuery<AVObject>(TableConstants.TABLE_FIRE)
        query.whereEqualTo(TableConstants.TOUSER,AVObject.createWithoutData(TableConstants.TABLE_USER,userObjectId))
        query.findInBackground(object : FindCallback<AVObject>(){

            override fun done(avObjects: MutableList<AVObject>?, avException: AVException?) {
                callback(avObjects,avException)
            }

        })
    }

    /**
     * 查询 RecentVisit 的信息
     */
    fun queryRecentVisitUser(userObjectId:String,callback:((Int,AVException?) -> Unit)){
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.USER,AVObject.createWithoutData(TableConstants.TABLE_USER,userObjectId))
        query.countInBackground(object : CountCallback(){
            override fun done(count: Int, e: AVException?) {
                callback(count,e)
            }
        })
    }

    /**
     * 查询 RecentVisit 的信息
     */
    fun queryRecentVisitVisit(userObjectId:String,callback:((Int,AVException?) -> Unit)){
        val query = AVQuery<AVObject>(TableConstants.TABLE_RECENTVISIT)
        query.whereEqualTo(TableConstants.VISITER,AVObject.createWithoutData(TableConstants.TABLE_USER,userObjectId))
        query.countInBackground(object : CountCallback(){
            override fun done(count: Int, e: AVException?) {
                callback(count,e)
            }
        })
    }

    /**
     * 查询 user 表
     */
    fun getFirstUser(callback: (AVObject?, AVException?) -> Unit){
        val query = AVQuery<AVObject>(TableConstants.TABLE_USER)
        query.getFirstInBackground(object : GetCallback<AVObject>(){

            override fun done(avUser: AVObject?, e: AVException?) {
                callback(avUser,e)
            }
        })
    }




}