package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      MineFragment
 * @Description:    我的fragments
 */

class MineFragment : BaseFragment() {

    companion object {
        fun getInstance():MineFragment{
            return MineFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_mine
    }

}