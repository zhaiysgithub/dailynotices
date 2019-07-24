package com.suncity.dailynotices.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import cn.leancloud.chatkit.LCChatKit
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
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
import com.suncity.dailynotices.utils.PreferenceStorage
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

    private var lastPos = -1
    private var ignorePos = 2 //tab 中忽略的位置

    companion object {

        const val EXTRA_POS = "extra_pos"
        const val POS_HOME = 0
        const val POS_DISCOVERY = 1
        const val POS_CENTER = 2
        const val POS_MSG = 3
        const val POS_MINE = 4
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_home
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    private var selectedPos : Int = -1

    override fun onStart() {
        super.onStart()
        selectedPos = intent?.getIntExtra(EXTRA_POS,-1) ?: -1
        Log.e("@@@","selectedPos = $selectedPos")
        val allCount = tablayout?.tabCount ?: 0
        if(selectedPos >= 0 && (selectedPos in 0 until allCount)){
            lastPos = selectedPos
            startAssignPos()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        selectedPos = intent?.getIntExtra(EXTRA_POS,-1) ?: -1
        Log.e("@@@","onNewIntent = $selectedPos")
        val allCount = tablayout?.tabCount ?: 0
        if(selectedPos >= 0 && (selectedPos in 0 until allCount)){
            lastPos = selectedPos
            startAssignPos()
        }
    }

    private fun startAssignPos(){
        val allCount = tablayout?.tabCount ?: 0
        val isAvailable = (lastPos in 0 until allCount)
        if(isAvailable){
            tablayout?.currentTab = lastPos
        }
    }

    override fun initData() {
        //判断是否已经登录了，已经登录的情况下需要每次都登录聊天室
        if(isLogined()){
            startLoginChatKit()
        }
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

        tablayout?.setTabData(mTabEntities, this@HomeActivity, R.id.container, mFragments, ignorePos)
        tablayout?.currentTab = 0
        lastPos = 0
    }

    private fun startLoginChatKit() {
        val currentUserObjectId = PreferenceStorage.userObjectId
        LCChatKit.getInstance().open(currentUserObjectId,object : AVIMClientCallback(){

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("LCChatKit.getInstance().open -> $e")
            }

        })
    }

    override fun initListener() {
        tablayout?.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabSelect(position: Int) {
                LogUtils.e("onTabSelect -> position = $position")
                startScale(position)
                if (!isLogined() && position == 3) {
                    return
                }
                lastPos = position
            }

            override fun onTabReselect(position: Int) {
                LogUtils.e("onTabReselect -> position = $position")
                startScale(position)
            }

        })
        iv_push?.setOnClickListener {
            if (!isLogined()) {
                startActivity(LoginActivity::class.java, false)
            }else{
                startActivity(PushDynamicActivity::class.java)
            }
        }
    }

    private fun startScale(position: Int) {
        val iconView = tablayout?.getIconView(position)
        if (iconView != null) {
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
