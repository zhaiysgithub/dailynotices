package com.suncity.dailynotices.ui.activity

import com.avos.avoscloud.AVUser
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.UserInfo
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import kotlinx.android.synthetic.main.ac_user_record.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UpdateRecordActivity
 * @Description:     作用描述
 * @UpdateDate:     16/7/2019
 */
class UpdateRecordActivity : BaseActivity() {

    private var mUserInfo:UserInfo? = null
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
        val currentUser = AVUser.getCurrentUser()
        val objectId = currentUser.objectId
        val username = currentUser.username
        et_stage_name?.setText(username)

        Query.queryUserInfoByObjectUserId(objectId){ userInfo, _ ->
            mUserInfo = userInfo
            if(userInfo != null){
                val sex = userInfo.sex ?: "0"
                if (sex == "1"){
                    et_sex?.setText("男")
                }else if(sex == "2"){
                    et_sex?.setText("女")
                }

                et_birth_date?.setText(userInfo.birthday)
                et_live_address?.setText(userInfo.living)
            }
        }
    }

    override fun initListener() {
        tv_next?.setOnClickListener {
            if(mUserInfo == null || PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
            UpdateRecordActivity2.start(this@UpdateRecordActivity,mUserInfo!!)
        }
    }
}