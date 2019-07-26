package com.suncity.dailynotices.ui.activity

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.AppBarStateChangeListener
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.dialog.BottomDialogiOSItem
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.lcoperation.Increase
import com.suncity.dailynotices.lcoperation.Modify
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.fragment.UserInfoHomeFragment
import com.suncity.dailynotices.ui.fragment.UserInfoPicFragment
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_userinfo2.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UserInfoActivity
 * @Description:    用户详情页面
 * @UpdateDate:     11/7/2019
 */
class UserInfoActivity : BaseActivity() {

    private var objectId: String? = null
    private var ivHeaderBg: String = ""
    private val tabTitles = arrayOf("主页", "图片")
    private var userInfoObject: AVObject? = null
    private var mAdapter: ViewPagerAdapter? = null
    private var autonym: Int = 0
    private val REQUEST_SETCOVER_CODE = 0
    private val REQUEST_SETAVATAR_CODE = 1
    private val REQUEST_SETPIC_CODE = 2
    private var isMine = false
    private var currentTabPos = 0

    companion object {
        private const val OBJECTID = "objectId"
        private const val IMGBG = "imgbg"
        private var mImgBg: String = ""
        /**
         * 此objectId是user对应的objectId
         */
        fun start(context: Context, objectId: String, imgBg: String? = null) {
            val intent = Intent()
            intent.setClass(context, UserInfoActivity::class.java)
            intent.putExtra(OBJECTID, objectId)
            mImgBg = imgBg ?: ""
            intent.putExtra(IMGBG, mImgBg)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalObserverHelper.addObserver(mObservable)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0f)
            .transparentStatusBar()
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_userinfo2
    }

    override fun initData() {
        objectId = intent.getStringExtra(OBJECTID)
        ivHeaderBg = intent.getStringExtra(IMGBG)
        isMine = (PreferenceStorage.userObjectId == objectId)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.mipmap.ico_nav_back)
        collapsing_toolbar?.isTitleEnabled = false
        collapsing_toolbar?.requestLayout()
        actionBarResponsive()

        mAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager?.offscreenPageLimit = (mAdapter?.count ?: 0)
        tabLayout?.setupWithViewPager(viewPager)

        queryData()
        if (StringUtils.isNotEmptyAndNull(objectId)) {
            saveRecentVister(objectId!!)
        }
    }

    /**
     * 保存查看和被查看的记录
     */
    private fun saveRecentVister(visterObjectId: String) {
        if(!isMine){
            Increase.createRecentVister(visterObjectId)
        }
    }

    private fun actionBarResponsive() {
        val actionBarHeight = ScreentUtils.getActionBarHeightPixel(this)
        val tabHeight = ScreentUtils.getTabHeight(this)
        if (actionBarHeight > 0) {
            toolbar?.layoutParams?.height = actionBarHeight + tabHeight
            toolbar?.requestLayout()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun queryData() {
        LogUtils.e("objectId = $objectId")
        if (objectId.isNullOrEmpty()) return
        Query.queryUserByObjectId(objectId!!) { avUser, e ->
            if (avUser != null) {
                val username = avUser.username
                val avatarFile: AVFile = avUser.getAVFile("avatar")
                val avatarUrl = avatarFile.url
                //TODO COVER 表的内容
                if (ivHeaderBg.isEmpty()) {
                    imageView_header?.setImageURI(avatarUrl)
                } else {
                    imageView_header?.setImageURI(ivHeaderBg)
                }
                iv_userinfo_avatar?.setImageURI(avatarUrl)
                tv_userinfo_name?.text = username
                toolbar_title?.text = username
                val userinfoId = avUser.getAVObject<AVObject>("info").objectId
                Query.queryUserInfoByObjectId(userinfoId) { userInfo, _ ->
                    if (userInfo != null) {
                        userInfoObject = userInfo
                        //设置底部的布局
                        viewPager?.adapter = mAdapter
                        val sex = userInfo.getString("sex")
                        if (sex == "1") {//女性
                            iv_userinfo_sex?.setBackgroundResource(R.drawable.shape_sex_girl)
                            iv_sex?.setImageResource(R.mipmap.ico_female)
                        } else {//男性
                            iv_userinfo_sex?.setBackgroundResource(R.drawable.shape_sex_boy)
                            iv_sex?.setImageResource(R.mipmap.ico_male)
                        }
                        var age = userInfo.getString("age")
                        if (StringUtils.isEmptyOrNull(age)) age = "18"
                        tv_age?.text = "${age}岁"

                        autonym = userInfo.getInt("autonym")
                        if (autonym == 1) {//已认证
                            tv_auth_mark?.text = Config.getString(R.string.str_certified_name)
                            val leftDrawable = Config.getDrawable(R.mipmap.ico_certification)
                            leftDrawable.setBounds(0, 0, leftDrawable.minimumWidth, leftDrawable.minimumHeight)
                            tv_auth_mark?.setCompoundDrawables(leftDrawable, null, null, null)
                        } else {//未认证
                            tv_auth_mark?.text = Config.getString(R.string.str_userinfo_no_autonym)
                            tv_auth_mark?.setCompoundDrawables(null, null, null, null)
                        }
                        val fireCount = userInfo.getInt("fire")
                        tv_userinfo_recommend?.text = "被推荐$fireCount"

                    } else {

                    }
                }
            }
        }
    }

    override fun initListener() {

        tv_direct_msg?.setOnClickListener {
            if (PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
            if (objectId == null) return@setOnClickListener
            if (isLogined()) {
                if (!isMine) {
                    startOpenLCIM(objectId!!)
                } else {
                    if (currentTabPos == 0) {
                        startActivity(PushDynamicActivity::class.java)
                    } else {
                        //进入图片选择和拍照的界面
                        val config = ISListConfig.Builder()
                            .multiSelect(true)
                            .rememberSelected(false)
                            .needCrop(false)
                            .build()

                        ISListActivity.startForResult(this@UserInfoActivity, config, REQUEST_SETPIC_CODE)
                    }
                }
            } else {
                LoginActivity.start(this@UserInfoActivity, -1)
            }

        }

        app_bar?.addOnOffsetChangedListener(mAppBarOffsetChangeListener)
        tv_auth_mark?.setOnClickListener {
            if (objectId == PreferenceStorage.userObjectId && !PreventRepeatedUtils.isFastDoubleClick()) {
                RealAuthActivity.start(this@UserInfoActivity, (autonym == 1))
            }

        }
        //设置封面
        if (isMine) {
            imageView_header?.setOnClickListener {
                createBottomDialog("设置封面", REQUEST_SETCOVER_CODE)
            }
            iv_userinfo_avatar?.setOnClickListener {
                createBottomDialog("设置头像", REQUEST_SETAVATAR_CODE)
            }
        } else {
            imageView_header?.setOnClickListener(null)
            iv_userinfo_avatar?.setOnClickListener(null)
        }

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (!isMine) return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (!isMine) return
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (!isMine || tab == null) return
                currentTabPos = tab.position
                if (currentTabPos == 0) {
                    tv_direct_msg.text = " + 演绎动态"
                } else {
                    tv_direct_msg.text = " + 展示图片"
                }
            }

        })

    }


    private fun createBottomDialog(itemText: String, requestCode: Int) {
        val bottomDialog = BottomDialogiOSItem(itemText, this@UserInfoActivity)
        bottomDialog.show()
        bottomDialog.setClickCallback(object : BottomDialogiOSItem.ClickCallback {
            override fun doItemClick() {
                //进入图片选择和拍照的界面
                val config = ISListConfig.Builder()
                    .multiSelect(false)
                    .rememberSelected(false)
                    .needCrop(false)
                    .build()

                ISListActivity.startForResult(this@UserInfoActivity, config, requestCode)
            }

            override fun doItemCancel() {
            }
        })
    }

    private fun startOpenLCIM(objectId: String) {
        if (null == LCChatKit.getInstance().client) {
            startLoginChatKit(objectId)
        } else {
            startLCIM(objectId)
        }
    }

    private fun startLoginChatKit(objectId: String) {
        LCChatKit.getInstance().open(objectId, object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("LCChatKit.getInstance().open -> $e")
                if (e == null) {
                    startLCIM(objectId)
                } else {
                    ToastUtils.showSafeToast(this@UserInfoActivity, "登录过期请重新登录")
                    LoginActivity.start(this@UserInfoActivity, -1)
                }
            }

        })
    }

    private fun startLCIM(bjectId: String) {
        val intent = Intent(this, LCIMConversationActivity::class.java)
        intent.putExtra(LCIMConstants.PEER_ID, bjectId)
        startActivity(intent)
    }

    private val mAppBarOffsetChangeListener = object : AppBarStateChangeListener() {

        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when (state) {
                State.COLLAPSED -> {
                    toolbar_title?.alpha = 1f
                    collapsing_toolbar?.setContentScrimColor(
                        ContextCompat.getColor(
                            this@UserInfoActivity,
                            R.color.colorPrimary
                        )
                    )
                }
                State.EXPANDED -> {
                    toolbar_title?.alpha = 0f
                    collapsing_toolbar?.setContentScrimColor(
                        ContextCompat.getColor(
                            this@UserInfoActivity,
                            android.R.color.transparent
                        )
                    )
                }
                else -> {
                }
            }
        }

        override fun onOffsetChanged(state: State, offset: Float) {
            when (state) {
                State.IDLE -> {
                    toolbar_title?.alpha = offset
                    collapsing_toolbar?.setContentScrimColor(
                        ArgbEvaluator().evaluate(
                            offset,
                            ContextCompat.getColor(
                                this@UserInfoActivity,
                                android.R.color.transparent
                            ), ContextCompat
                                .getColor(
                                    this@UserInfoActivity,
                                    R.color.colorPrimary
                                )
                        ) as Int
                    )
                }
                else -> {
                    setAlphaForView(toolbar_title, offset)
                    collapsing_toolbar?.setContentScrimColor(
                        ArgbEvaluator()
                            .evaluate(
                                offset, ContextCompat.getColor(
                                    this@UserInfoActivity,
                                    android.R.color.transparent
                                ), ContextCompat
                                    .getColor(this@UserInfoActivity, R.color.colorPrimary)
                            ) as Int
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_SETCOVER_CODE -> {
                    val pathList = data.getStringArrayListExtra("result")
                    if (pathList != null && pathList.size > 0) {
                        val coverPath = pathList[0]
                        if (StringUtils.isNotEmptyAndNull(coverPath)) {
                            imageView_header?.setImageURI("file://$coverPath")
                            Modify.updateCoverFile(PreferenceStorage.userObjectId, coverPath) { _, e ->
                                ToastUtils.showSafeToast(
                                    this@UserInfoActivity,
                                    if (e == null) "上传成功" else Constants.ERROR_MSG
                                )
                            }
                        }
                    }
                }

                REQUEST_SETAVATAR_CODE -> {
                    val pathList = data.getStringArrayListExtra("result")
                    if (pathList != null && pathList.size > 0) {
                        val avatarPath = pathList[0]
                        if (StringUtils.isNotEmptyAndNull(avatarPath)) {
                            iv_userinfo_avatar?.setImageURI("file://$avatarPath")
                            Modify.updateAvatarFile(PreferenceStorage.userObjectId, avatarPath) { _, e ->
                                ToastUtils.showSafeToast(
                                    this@UserInfoActivity,
                                    if (e == null) "上传成功" else Constants.ERROR_MSG
                                )
                            }
                        }
                    }
                }

                REQUEST_SETPIC_CODE -> {
                    val pathList = data.getStringArrayListExtra("result")
                    if (pathList != null && pathList.size > 0) {
                        GlobalObserverHelper.onUserPicUpdateListener(pathList)
                    }
                }
            }
        }
    }


    private fun setAlphaForView(v: View?, alpha: Float) {
        val alphaAnimation = AlphaAnimation(alpha, alpha)
        alphaAnimation.duration = 0
        alphaAnimation.fillAfter = true
        v?.startAnimation(alphaAnimation)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> {
                    UserInfoHomeFragment.getInstance(userInfoObject)
                }
                1 -> {
                    UserInfoPicFragment.getInstance(userInfoObject)
                }
                else -> {
                    null
                }
            }
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }
    }

    private val mObservable = object : SimpleGlobalObservable() {

        override fun onLoginSuccess() {
            queryData()
        }

        override fun onLogoutSuccess() {
            queryData()
        }

        override fun onUpdateUserinfoSuccess() {
            queryData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObserverHelper.removeObserver(mObservable)
    }

}