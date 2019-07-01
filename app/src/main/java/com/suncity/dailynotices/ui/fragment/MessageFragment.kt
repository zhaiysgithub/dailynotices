package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

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

    override fun setContentView(): Int {
        return R.layout.fg_msg
    }

}