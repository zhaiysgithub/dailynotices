package com.suncity.dailynotices.ui.activity

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.CircleTimerView
import kotlinx.android.synthetic.main.ac_splash.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SplashActivity
 * @Description:    启动页
 */

class SplashActivity : BaseActivity() {

    override fun setScreenManager() {
//        isFullScreen = true
        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarDarkIcon(true, 0.2f)
            .init()
    }

    override fun getActivityLayoutId() : Int {
        return R.layout.ac_splash
    }

    override fun initData() {
        circle_timer?.setOnCountDownFinish(countDownFinish)
        circle_timer?.start()
    }

    override fun initListener() {
        circle_timer?.setOnClickListener {
            circle_timer.cancelAnim()
            startActivity(HomeActivity::class.java,true)
        }
    }

    private val countDownFinish = object : CircleTimerView.OnCountDownFinish{
        override fun onFinish() {
            startActivity(HomeActivity::class.java,true)
        }
    }
}