package com.suncity.dailynotices.ui.dialog

import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.LogUtils

/**
 * 抽取公用的FragmentDialog
 */
abstract class BaseFragmentDialog : DialogFragment() {

    private var isShowing = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Window相关
        if (dialog?.window != null) {
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(context!!, android.R.color.transparent))
            //动画
            val animations = initAnimations()
            if (animations != 0) {
                dialog?.window?.setWindowAnimations(animations)
            }
        }
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MNCustomDialog)
        //隐藏title
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //点击外部不可取消
        dialog?.setCanceledOnTouchOutside(false)
        //拦截外部返回
        dialog?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        //初始化其他可以覆盖上面
        initDialog()
        return initView(inflater)
    }


    protected abstract fun initView(inflater: LayoutInflater): View

    fun initDialog() {

    }

    protected abstract fun initAnimations(): Int

    fun initBackgroundAlpha(): Float {
        return 0.6f
    }

    override fun dismiss() {
        isShowing = false
        super.dismiss()
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        isShowing = false
    }

    fun showDialog(mActivity: FragmentActivity?) {
        try {
            if (isShowing()) {
                return
            }
            if (mActivity != null && mActivity.supportFragmentManager != null) {
                val supportFragmentManager = mActivity.supportFragmentManager
                //在每个add事务前增加一个remove事务，防止连续的add
                supportFragmentManager.beginTransaction().remove(this).commit()
                show(supportFragmentManager, mActivity.localClassName)
            }
        } catch (e: Exception) {
            LogUtils.e(e.toString())
        }

    }

    override fun show(manager: FragmentManager, tag: String?) {
        isShowing = true
        super.show(manager, tag)
    }


    fun isShowing(): Boolean {
        return isShowing || dialog != null && dialog?.isShowing == true
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val windowParams = window.attributes
            windowParams.dimAmount = initBackgroundAlpha()
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT   //设置宽度充满屏幕
            windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = windowParams
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isShowing = false
    }

}
