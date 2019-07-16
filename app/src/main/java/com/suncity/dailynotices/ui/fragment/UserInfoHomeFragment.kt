package com.suncity.dailynotices.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.avos.avoscloud.AVObject
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      UserInfoFragment
 * @Description:    用户中心的主页fragment
 * @UpdateDate:     12/7/2019
 */

class UserInfoHomeFragment : BaseFragment() {

    companion object {
        private const val ARGUMENT_TAG = "argument_tag"
        fun getInstance(userInfo: AVObject?): UserInfoHomeFragment {
            val userInfoHomeFragment = UserInfoHomeFragment()
            if(userInfo != null){
                val bundle = Bundle()
                bundle.putParcelable(ARGUMENT_TAG, userInfo)
                userInfoHomeFragment.arguments = bundle
            }
            return userInfoHomeFragment
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_userinfo
    }

    override fun initData() {

    }

    override fun initListener() {


    }


}