package com.suncity.dailynotices.ui.activity

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      MineRecommentActivity
 * @Description:    推荐我的页面
 * @UpdateDate:     10/7/2019
 */

class MineRecommentActivity : BaseActivity() {


    override fun setScreenManager() {

        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true,0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_mine_recomment
    }

}