package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.utils.Config
import kotlinx.android.synthetic.main.view_empty.*

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

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_reds)
    }
}