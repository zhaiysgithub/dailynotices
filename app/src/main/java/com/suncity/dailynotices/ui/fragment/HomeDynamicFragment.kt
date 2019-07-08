package com.suncity.dailynotices.ui.fragment

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import kotlinx.android.synthetic.main.view_empty.*

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

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_dynamic)
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