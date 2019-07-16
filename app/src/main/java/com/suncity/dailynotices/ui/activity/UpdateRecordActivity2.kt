package com.suncity.dailynotices.ui.activity

import android.content.Context
import android.content.Intent
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.UserInfo
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.StringUtils
import kotlinx.android.synthetic.main.ac_update_record2.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UpdateRecordActivity2
 * @Description:     作用描述
 * @UpdateDate:     16/7/2019
 */
class UpdateRecordActivity2 : BaseActivity() {

    private var userInfo: UserInfo? = null

    companion object {
        private const val BUNDLE_USERINFO = "bundle_userinfo"
        fun start(context: Context, userInfo: UserInfo) {
            val intent = Intent()
            intent.setClass(context, UpdateRecordActivity2::class.java)
            intent.putExtra(BUNDLE_USERINFO, userInfo)
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

    override fun initData() {
        userInfo = intent?.getSerializableExtra(BUNDLE_USERINFO) as UserInfo

        val height = userInfo?.height
        if(StringUtils.isNotEmptyAndNull(height)){
            et_update_height?.setText("${height}cm")
        }
        val weight = userInfo?.weight
        if(StringUtils.isNotEmptyAndNull(weight)){
            et_update_weight?.setText("${weight}kg")
        }

        val bwh = userInfo?.bwh
        if(StringUtils.isNotEmptyAndNull(bwh)){
            et_update_bwh?.setText("$bwh")
        }

        val shoes = userInfo?.shoeSize
        if(StringUtils.isNotEmptyAndNull(shoes)){
            et_update_shoes?.setText("$shoes")
        }

        val nation = userInfo?.nation
        if(StringUtils.isNotEmptyAndNull(nation)){
            et_update_national?.setText("$nation")
        }

        val nativePlace = userInfo?.native
        if(StringUtils.isNotEmptyAndNull(nativePlace)){
            et_update_nativeplace?.setText("$nativePlace")
        }

        val country = userInfo?.nationality
        if(StringUtils.isNotEmptyAndNull(country)){
            et_update_country?.setText("$country")
        }

        val university = userInfo?.graduation
        if(StringUtils.isNotEmptyAndNull(university)){
            et_update_university?.setText("$university")
        }

    }


    override fun initListener() {

        tv_complete?.setOnClickListener {
            //TODO 保存信息到userInfo中
            val objectId = PreferenceStorage.userObjectId
            UserInfoActivity.start(this@UpdateRecordActivity2, objectId)
        }
    }
}