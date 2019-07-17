package com.suncity.dailynotices.ui.activity

import com.avos.avoscloud.AVUser
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.UserInfo
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import kotlinx.android.synthetic.main.ac_user_record.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UpdateRecordActivity
 * @Description:     作用描述
 * @UpdateDate:     16/7/2019
 */
class UpdateRecordActivity : BaseActivity() {


    private var hasEdit = false
    private var mUserInfo: UserInfo? = null
    private var databaseUserName: String? = null
    private var databaseUserSex: String? = null
    private var databaseUserBirth: String? = null
    private var databaseUserAddress: String? = null
    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .keyboardEnable(true)
            .init()

    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_user_record
    }


    override fun initData() {

        tv_title_center?.text = "个人档案"

        val currentUser = AVUser.getCurrentUser()
        val objectId = currentUser.objectId
        val username = currentUser.username
        databaseUserName = username
        et_stage_name?.setText(username)

        Query.queryUserInfoByObjectUserId(objectId) { userInfo, _ ->
            mUserInfo = userInfo
            if (userInfo != null) {
                val sex = userInfo.sex ?: "0"
                et_sex?.setText(getSexValue(sex))

                et_birth_date?.setText(getBirthday(userInfo.birthday))
                et_live_address?.setText(getLiving(userInfo.living))
            }
        }
    }

    private fun getSexValue(sexValue: String): String? {
        databaseUserSex = when (sexValue) {
            "1" -> "男"
            "2" -> "女"
            else -> null
        }

        return databaseUserSex
    }

    private fun getBirthday(birthday:String?):String?{
        databaseUserBirth = birthday
        return databaseUserBirth
    }

    private fun getLiving(living:String?):String?{
        databaseUserAddress = living
        return databaseUserAddress
    }

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            finish()
        }

        tv_next?.setOnClickListener {
            if (mUserInfo == null || PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
            val userNameEdit = et_stage_name?.text?.toString()?.trim()
            val sexEdit = et_sex?.text?.toString()?.trim()
            val birthdayEdit = et_birth_date?.text?.toString()?.trim()
            val liveAddressEdit = et_live_address?.text?.toString()?.trim()
            val modify = hasModify(userNameEdit, sexEdit, birthdayEdit, liveAddressEdit)
            val userNameUpdate = userNameEdit(userNameEdit)
            UpdateRecordActivity2.start(
                this@UpdateRecordActivity,
                mUserInfo!!,
                userNameEdit,
                sexEdit,
                birthdayEdit,
                liveAddressEdit,
                modify,
                userNameUpdate
            )
        }
    }

    private fun hasModify(
        userNameEdit: String?,
        sexEdit: String?,
        birthdayEdit: String?,
        liveAddressEdit: String?
    ): Boolean {
        hasEdit =
            (databaseUserName != userNameEdit || databaseUserSex != sexEdit || databaseUserBirth != birthdayEdit || databaseUserAddress != liveAddressEdit)
        return hasEdit
    }

    private fun userNameEdit(userNameEdit: String?): Boolean {
        return (databaseUserName != userNameEdit)
    }
}