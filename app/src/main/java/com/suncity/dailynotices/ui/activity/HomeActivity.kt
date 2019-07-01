package com.suncity.dailynotices.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.app.Fragment
import android.view.View
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback
import com.suncity.dailynotices.ui.views.immersionBar
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.OnTabSelectListener
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.fragment.DiscoveryFragment
import com.suncity.dailynotices.ui.fragment.HomeFragment
import com.suncity.dailynotices.ui.fragment.MessageFragment
import com.suncity.dailynotices.ui.fragment.MineFragment
import com.suncity.dailynotices.ui.views.tablayout.CustomTabEntity
import com.suncity.dailynotices.ui.views.tablayout.TabEntity
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.ToastUtils
import com.suncity.dailynotices.utils.UIUtils
import kotlinx.android.synthetic.main.ac_home.*


class HomeActivity : BaseActivity() {

    private var homeTexts: Array<String>? = null
    private var mTabEntities = ArrayList<CustomTabEntity>()
    private var mFragments = arrayListOf<Fragment?>()
    private var unSelectedRes = arrayListOf(
        R.mipmap.ico_home_normal, R.mipmap.ico_discovery_normal
        , R.mipmap.ico_home_center, R.mipmap.ico_msg_normal, R.mipmap.ico_mine_normal
    )

    private var selectedRes = arrayListOf(
        R.mipmap.ico_home_selected, R.mipmap.ico_discovery_selected
        , R.mipmap.ico_home_center, R.mipmap.ico_msg_selected, R.mipmap.ico_mine_selected
    )

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_home
    }

    override fun setScreenManager() {
        super.setScreenManager()
        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarDarkIcon(true, 0.2f)
            .init()
    }

    override fun initData() {
        initAvoscloud()
        homeTexts = UIUtils.getStringArray(R.array.home_bottom_text)
        val size = homeTexts?.size ?: return
        for (i in 0 until size) {
            val text = homeTexts?.get(i) ?: ""
            val selectedIcon = selectedRes[i]
            val unSelectedIcon = unSelectedRes[i]
            mTabEntities.add(TabEntity(text, selectedIcon, unSelectedIcon))
        }
        mFragments.clear()
        mFragments.add(HomeFragment.getInstance())
        mFragments.add(DiscoveryFragment.getInstance())
        mFragments.add(null)
        mFragments.add(MessageFragment.getInstance())
        mFragments.add(MineFragment.getInstance())

        val centerView = tablayout.getChildAt((size / 2)) // 中间的位置隐藏
        centerView?.visibility = View.GONE

        tablayout?.setTabData(mTabEntities, this@HomeActivity, R.id.container, mFragments,2)
        tablayout?.currentTab = 0
    }

    override fun initListener() {
        tablayout?.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabSelect(position: Int) {
                LogUtils.e("onTabSelect -> position = $position")
                startScale(position)
            }

            override fun onTabReselect(position: Int) {
                LogUtils.e("onTabReselect -> position = $position")
                startScale(position)
            }

        })
        iv_push?.setOnClickListener {
            ToastUtils.show("IV_PUSH")
        }
    }

    private fun startScale(position: Int){
        val iconView = tablayout?.getIconView(position)
        if (iconView != null){
            startScaleAnim(iconView)
        }
    }

    private fun startScaleAnim(view: View) {
        val scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.6f, 1f)
        val scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.6f, 1f)
        val set = AnimatorSet()
        set.play(scaleXAnim).with(scaleYAnim)
        set.duration = 400
        set.start()
    }

    private fun initAvoscloud() {
        // 测试 SDK 是否正常工作的代码
        val testObject = AVObject("TestObject")
        testObject.put("words", "Hello World!")
        testObject.saveInBackground(object : SaveCallback() {

            override fun done(e: AVException?) {
                if (null == e) {
                    LogUtils.e("AVObject -> success")
                } else {
                    LogUtils.e("AVObject -> error -> $e")
                }
            }

        })
    }

}
