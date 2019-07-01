package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeNoticeFragment
 * @Description:    通告
 */

class HomeNoticeFragment : BaseFragment() {

    companion object {

        fun getInstance():HomeNoticeFragment{
            return HomeNoticeFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home_notice
    }

}