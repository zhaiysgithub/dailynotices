package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeDynamicFragment
 * @Description:    动态
 */
class HomeDynamicFragment : BaseFragment() {

    companion object {

        fun getInstance():HomeDynamicFragment{
            return HomeDynamicFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home_dynamic
    }
}