package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      DiscoveryFragment
 * @Description:    发现的fragments
 */

class DiscoveryFragment : BaseFragment() {

    companion object {
        fun getInstance():DiscoveryFragment{
            return DiscoveryFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_discovery
    }

}