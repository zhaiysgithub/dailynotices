package com.suncity.dailynotices.ui.views.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.views.tablayout
 * @ClassName:      FragmentChangeManager
 * @Description:    管理 tab
 */

class FragmentChangeManager(fm: FragmentManager, containerViewId: Int, fragments: ArrayList<Fragment?>) {

    private var mFragmentManager: FragmentManager = fm
    private var mContainerViewId: Int = containerViewId
    /** Fragment切换数组  */
    private var mFragments: ArrayList<Fragment?> = fragments
    /** 当前选中的Tab  */
    private var mCurrentTab: Int = 0

    init {
        initFragments()
    }

    /** 初始化fragments  */
    private fun initFragments() {
        mFragments.forEach {
            if(it != null){
                mFragmentManager.beginTransaction().add(mContainerViewId, it).hide(it).commit()
            }
        }
        setFragments(0)
    }

    /** 界面切换控制  */
    fun setFragments(index: Int) {
        if (mFragments.size == 0) return
        for (i in 0 until mFragments.size) {
            val ft = mFragmentManager.beginTransaction()
            val fragment = mFragments[i]
            if (fragment != null){
                if (i == index) {
                    ft.show(fragment)
                } else {
                    ft.hide(fragment)
                }
                ft.commit()
            }
        }
        mCurrentTab = index
    }

    fun getCurrentTab(): Int {
        return mCurrentTab
    }

    fun getCurrentFragment(): Fragment? {
        return mFragments[mCurrentTab]
    }

}