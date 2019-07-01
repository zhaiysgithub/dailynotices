package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeRedsFragment
 * @Description:    红人
 */
class HomeRedsFragment : BaseFragment() {

    companion object {

        fun getInstance():HomeRedsFragment{
            return HomeRedsFragment()
        }
    }
    override fun setContentView(): Int {
        return R.layout.fg_home_reds
    }
}