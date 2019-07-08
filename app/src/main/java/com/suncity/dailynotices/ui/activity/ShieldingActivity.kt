package com.suncity.dailynotices.ui.activity

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      ShieldingActivity
 * @Description:    屏蔽过的人
 * @UpdateDate:     5/7/2019
 */
class ShieldingActivity : BaseActivity() {

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true,0f)
            .statusBarColor(R.color.color_white)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_shield
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
    }
}