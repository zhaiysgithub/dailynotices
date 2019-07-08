package com.suncity.dailynotices.ui.activity

import android.text.method.ScrollingMovementMethod
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import kotlinx.android.synthetic.main.ac_user_agreement.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UserAggreementActivity
 * @Description:    用户协议页面
 */
class UserAgreementActivity : BaseActivity() {

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_user_agreement
    }

    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_agreement_title)
        textView?.text = Config.getString(R.string.str_user_agreement)
        textView?.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

    }
}