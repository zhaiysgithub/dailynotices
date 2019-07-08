package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.SystemUtils
import kotlinx.android.synthetic.main.ac_about.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      AboutAppActivity
 * @Description:    关于页面
 * @UpdateDate:     5/7/2019
 */
class AboutAppActivity : BaseActivity() {

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true,0f)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_about
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_about)
        tv_about_version.text = "v${SystemUtils.versionName}"
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
    }
}