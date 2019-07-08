package com.suncity.dailynotices.ui.fragment

import android.view.View
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

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

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_data)
        tv_title_center?.text = Config.getString(R.string.str_title_discovery)
        fl_title_back?.visibility = View.GONE
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true,0f)
                .init()
        }
    }

}