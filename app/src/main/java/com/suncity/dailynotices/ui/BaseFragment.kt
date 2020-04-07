package com.suncity.dailynotices.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    /**
     * @param cls 需要打开的页面 class
     * @param isFinish 打开之前是否需要关闭当前页面
     */
    fun startActivity(cls: Class<*>, isFinish: Boolean = false) {
        startActivity(cls, -1, isFinish)
    }

    /**
     * @param cls 需要打开的页面 class
     * @param requestCode 需要传递过去的 requestCode
     * @param isFinish 打开之前是否需要关闭当前页面
     */
    fun startActivity(cls: Class<*>, requestCode: Int = -1, isFinish: Boolean = false) {
        val intent = Intent(requireContext(), cls)
        if (requestCode == -1) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
        if (isFinish) {
            activity?.finish()
        }
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