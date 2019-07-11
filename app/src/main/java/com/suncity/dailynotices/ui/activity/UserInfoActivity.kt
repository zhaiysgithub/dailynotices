package com.suncity.dailynotices.ui.activity

import android.content.Context
import android.content.Intent
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UserInfoActivity
 * @Description:    用户详情页面
 * @UpdateDate:     11/7/2019
 */
class UserInfoActivity : BaseActivity() {

    companion object {
        private const val OBJECTID = "objectId"

        fun start(context: Context,objectId:String) {
            val intent = Intent()
            intent.setClass(context,UserInfoActivity::class.java)
            intent.putExtra(OBJECTID,objectId)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .transparentBar()
            .fitsSystemWindows(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_userinfo
    }
}