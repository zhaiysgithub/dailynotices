package com.suncity.dailynotices.ui.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.UIUtils
import kotlinx.android.synthetic.main.fg_home.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeFragment
 * @Description:    首页的fragments
 */

class HomeFragment : BaseFragment() {

    private val mTitles = UIUtils.getStringArray(R.array.home_home_text)

    private val mFragments = arrayListOf<Fragment>()

    //指定显示的位置
    private var currentItemPage = 0

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home
    }

    override fun initData() {
        mFragments.clear()
        mFragments.add(HomeDynamicFragment.getInstance())
        mFragments.add(HomeNoticeFragment.getInstance())
        mFragments.add(HomeRedsFragment.getInstance())

        val homeAdapter = HomePagerAdapter(childFragmentManager, mFragments, mTitles)
        viewPager_home?.adapter = homeAdapter
        viewPager_home?.currentItem = currentItemPage
        viewPager_home?.offscreenPageLimit = mFragments.size
        tablayout_home?.setViewPager(viewPager_home,mTitles)
    }

    class HomePagerAdapter(
        fm: FragmentManager, private val fragments: ArrayList<Fragment>
        , private val mTitles: Array<String>
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }
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