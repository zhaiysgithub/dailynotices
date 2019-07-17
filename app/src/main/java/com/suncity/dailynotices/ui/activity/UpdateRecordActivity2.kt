package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import com.suncity.dailynotices.R
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.dialog.OnDismissListener
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.model.UserInfo
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.StringUtils
import kotlinx.android.synthetic.main.ac_update_record2.*
import kotlinx.android.synthetic.main.view_title.*
import java.lang.Exception
import kotlin.collections.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UpdateRecordActivity2
 * @Description:     作用描述
 * @UpdateDate:     16/7/2019
 */
class UpdateRecordActivity2 : BaseActivity() {

    private var userInfo: UserInfo? = null
    private var userInfoObjectId: String? = null
    private var userNameEdit: String? = null
    private var sexEdit: String? = null
    private var birthdayEdit: String? = null
    private var liveAddressEdit: String? = null
    private var hasModify: Boolean = false
    private var hasEdit: Boolean = false
    private var hasUserNameUpdate: Boolean = false


    private var databaseHeight: String? = null
    private var databaseWeight: String? = null
    private var databaseBwh: String? = null
    private var databaseShoes: String? = null
    private var databaseNation: String? = null
    private var databaseNativePlace: String? = null
    private var databaseCountry: String? = null
    private var databaseUniversity: String? = null

    companion object {
        private const val BUNDLE_USERINFO = "bundle_userinfo"
        private const val BUNDLE_SEXEDIT = "sexedit"
        private const val BUNDLE_USERNAMEEDIT = "usernameedit"
        private const val BUNDLE_BIRTHEDIT = "birthedit"
        private const val BUNDLE_LIVEADDRESS = "liveaddress"
        private const val BUNDLE_HASMODIFY = "hasmodify"
        private const val BUNDLE_USERNAME_UPDATE = "username_update"
        fun start(
            context: Context,
            userInfo: UserInfo,
            userNameEdit: String?
            ,
            sexEdit: String?,
            birthdayEdit: String?,
            liveAddressEdit: String?,
            hasModify: Boolean,
            userNameUpdate: Boolean
        ) {
            val intent = Intent()
            intent.setClass(context, UpdateRecordActivity2::class.java)
            intent.putExtra(BUNDLE_USERINFO, userInfo)
            intent.putExtra(BUNDLE_USERNAMEEDIT, userNameEdit)
            intent.putExtra(BUNDLE_SEXEDIT, sexEdit)
            intent.putExtra(BUNDLE_BIRTHEDIT, birthdayEdit)
            intent.putExtra(BUNDLE_LIVEADDRESS, liveAddressEdit)
            intent.putExtra(BUNDLE_HASMODIFY, hasModify)
            intent.putExtra(BUNDLE_USERNAME_UPDATE, userNameUpdate)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .keyboardEnable(true)
            .init()

    }

    override fun getActivityLayoutId(): Int {

        return R.layout.ac_update_record2
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        tv_title_center?.text = "个人档案"

        userInfo = intent?.getSerializableExtra(BUNDLE_USERINFO) as UserInfo
        userNameEdit = intent?.getStringExtra(BUNDLE_USERNAMEEDIT)
        sexEdit = intent?.getStringExtra(BUNDLE_SEXEDIT)
        birthdayEdit = intent?.getStringExtra(BUNDLE_BIRTHEDIT)
        liveAddressEdit = intent?.getStringExtra(BUNDLE_LIVEADDRESS)
        hasModify = intent?.getBooleanExtra(BUNDLE_HASMODIFY, false) ?: false
        hasUserNameUpdate = intent?.getBooleanExtra(BUNDLE_USERNAME_UPDATE, false) ?: false

        userInfoObjectId = userInfo?.objectId

        databaseHeight = userInfo?.height
        if (StringUtils.isNotEmptyAndNull(databaseHeight)) {
            et_update_height?.setText("${databaseHeight}cm")
        }
        databaseWeight = userInfo?.weight
        if (StringUtils.isNotEmptyAndNull(databaseWeight)) {
            et_update_weight?.setText("${databaseWeight}kg")
        }

        databaseBwh = userInfo?.bwhString
        if (StringUtils.isNotEmptyAndNull(databaseBwh)) {
            et_update_bwh?.setText("$databaseBwh")
        }

        databaseShoes = userInfo?.shoeSize
        if (StringUtils.isNotEmptyAndNull(databaseShoes)) {
            et_update_shoes?.setText("$databaseShoes")
        }

        databaseNation = userInfo?.nation
        if (StringUtils.isNotEmptyAndNull(databaseNation)) {
            et_update_national?.setText("$databaseNation")
        }

        databaseNativePlace = userInfo?.native
        if (StringUtils.isNotEmptyAndNull(databaseNativePlace)) {
            et_update_nativeplace?.setText("$databaseNativePlace")
        }

        databaseCountry = userInfo?.nationality
        if (StringUtils.isNotEmptyAndNull(databaseCountry)) {
            et_update_country?.setText("$databaseCountry")
        }

        databaseUniversity = userInfo?.graduation
        if (StringUtils.isNotEmptyAndNull(databaseUniversity)) {
            et_update_university?.setText("$databaseUniversity")
        }

    }


    override fun initListener() {

        fl_title_back?.setOnClickListener {
            finish()
        }

        tv_complete?.setOnClickListener {
            //判断是否有修改，有修改保存，无修改直接跳过保存

            val height = et_update_height?.text?.toString()?.trim()
            val weight = et_update_weight?.text?.toString()?.trim()
            val bwh = et_update_bwh?.text?.toString()?.trim()
            val shoes = et_update_shoes?.text?.toString()?.trim()
            val nation = et_update_national?.text?.toString()?.trim()
            val nativePlace = et_update_nativeplace?.text?.toString()?.trim()
            val country = et_update_country?.text?.toString()?.trim()
            val university = et_update_university?.text?.toString()?.trim()
            val edit = hasUpdate(height, weight, bwh, shoes, nation, nativePlace, country, university)
            if (hasUserNameUpdate) {
                //更新user表
                val currentUser = AVUser.getCurrentUser()
                currentUser?.username = userNameEdit
                currentUser.saveInBackground(object : SaveCallback() {
                    override fun done(e: AVException?) {
                        if (e == null) {
                            PreferenceStorage.userName = userNameEdit ?: ""
                        }
                        LogUtils.e("更新用户表成功")
                        //更新usreInfo表
                        updateUserInfo(
                            edit, sexEdit, birthdayEdit, liveAddressEdit, height, weight, bwh, shoes
                            , nation, nativePlace, country, university
                        )
                    }
                })
            } else {
                updateUserInfo(
                    edit, sexEdit, birthdayEdit, liveAddressEdit, height, weight, bwh, shoes
                    , nation, nativePlace, country, university
                )
            }
        }
    }

    private fun updateUserInfo(
        edit: Boolean, sexEdit: String?,
        birthdayEdit: String?,
        liveAddressEdit: String?,
        height: String?,
        weight: String?,
        bwh: String?,
        shoes: String?,
        nation: String?,
        nativePlace: String?,
        country: String?,
        university: String?
    ) {

        if (edit || hasModify) {
            //更新数据库
            if (StringUtils.isEmptyOrNull(userInfoObjectId)) {
                startUserInfoActivity()
            } else {
                updateUserInfo2DB(
                    sexEdit, birthdayEdit, liveAddressEdit, height, weight, bwh, shoes
                    , nation, nativePlace, country, university
                ) {
                    //跳转
                    startUserInfoActivity()
                }
            }
        } else {
            //跳转
            startUserInfoActivity()
        }
    }

    private fun updateUserInfo2DB(
        sexEdit: String?,
        birthdayEdit: String?,
        liveAddressEdit: String?,
        height: String?,
        weight: String?,
        bwh: String?,
        shoes: String?,
        nation: String?,
        nativePlace: String?,
        country: String?,
        university: String?, callback: (AVException?) -> Unit
    ) {
        val dbObject = AVObject.createWithoutData(TableConstants.TABLE_USERINFO, userInfoObjectId)
        dbObject.put("sex", getSexValue(sexEdit))
        dbObject.put("birthday", birthdayEdit)
        dbObject.put("living", liveAddressEdit)
        dbObject.put("height", height)
        dbObject.put("weight", weight)
        dbObject.put("bwh", getBwhArray(bwh))
        dbObject.put("showSize", shoes)
        dbObject.put("nation", nation)
        dbObject.put("native", nativePlace)
        dbObject.put("nationality", country)
        dbObject.put("graduation", university)
        dbObject.saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                if (e != null) {
                    LogUtils.e("更新UserInfo表失败：$e")
                } else {
                    LogUtils.e("更新UserInfo表成功")
                }
                callback(e)
            }
        })
    }

    private fun getSexValue(sexValue: String?): String {
        return when (sexValue) {
            "男" -> "1"
            "女" -> "2"
            else -> "0"
        }
    }

    private fun getBwhArray(bwh: String?): Array<String>? {
        if (StringUtils.isEmptyOrNull(bwh)) return null
        try {
            val splitBwh = bwh?.split("-") ?: return null
            return if (splitBwh is ArrayList<String>) {
                splitBwh.toArray() as Array<String>?
            } else {
                null
            }
        } catch (e: Exception) {
            LogUtils.e("getBwhArray Error -> e")
            return null
        }
    }

    private fun startUserInfoActivity() {
        GlobalObserverHelper.updateUserInfo()
        TipDialog.show(this@UpdateRecordActivity2, "保存成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
            .setOnDismissListener(object : OnDismissListener {
                override fun onDismiss() {
                    val objectId = PreferenceStorage.userObjectId
                    UserInfoActivity.start(this@UpdateRecordActivity2, objectId)
                }

            })

    }


    private fun hasUpdate(
        height: String?,
        weight: String?,
        bwh: String?,
        shoes: String?,
        nation: String?,
        nativePlace: String?,
        country: String?,
        university: String?
    ): Boolean {
        hasEdit = (databaseHeight != height || databaseWeight != weight || databaseBwh != bwh || databaseShoes != shoes
                || databaseNation != nation || databaseNativePlace != nativePlace || databaseCountry != country || databaseUniversity != university)
        return hasEdit
    }




}