package com.suncity.dailynotices.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suncity.dailynotices.utils.DisplayUtils
import com.suncity.dailynotices.utils.PreferenceStorage


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui
 * @ClassName:      BaseFragment
 * @Description:    Fragments的基类
 */

abstract class BaseFragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(setContentView(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()
    }

    open fun initListener() { }

    open fun initData() { }

    abstract fun setContentView(): Int

    protected fun isLogined(): Boolean {
        return PreferenceStorage.isLogin
    }

    override fun onPause() {
        super.onPause()
        DisplayUtils.hiddenInputMethod(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
}