package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.LoginActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.LogUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      MessageFragment
 * @Description:    消息的fragments
 */

class MessageFragment : BaseFragment() {


    companion object {
        fun getInstance():MessageFragment{
            return MessageFragment()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtils.e("MessageFragment -> hidden = $hidden")
        if(!hidden){
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true,0f)
                .init()
        }
        if(!hidden && !isLogined()){
            startActivity(LoginActivity::class.java, false)
        }
    }
    override fun setContentView(): Int {
        return R.layout.fg_msg
    }
}