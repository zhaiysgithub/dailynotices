package com.suncity.dailynotices.ui.activity

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVObject
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.AppBarStateChangeListener
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.fragment.UserInfoHomeFragment
import com.suncity.dailynotices.ui.fragment.UserInfoPicFragment
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import com.suncity.dailynotices.utils.ScreentUtils
import com.suncity.dailynotices.utils.StringUtils
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
    private var userInfoObject:AVObject? = null
    private var mAdapter: ViewPagerAdapter? = null

    companion object {
        private const val OBJECTID = "objectId"
        private const val IMGBG = "imgbg"
        private var mImgBg : String = ""
        fun start(context: Context, objectId: String, imgBg: String? = null) {
            val intent = Intent()
            intent.setClass(context, UserInfoActivity::class.java)
            intent.putExtra(OBJECTID, objectId)
            mImgBg = imgBg ?: ""
            intent.putExtra(IMGBG,mImgBg)
            context.startActivity(intent)
        }
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
        if (objectId.isNullOrEmpty()) return
        Query.queryUserByObjectId(objectId!!) { avUser, e ->
            if (avUser != null) {
                val username = avUser.username
                val avatarFile: AVFile = avUser.getAVFile("avatar")
                val avatarUrl = avatarFile.url
                if(ivHeaderBg.isEmpty()){
                    imageView_header?.setImageURI(avatarUrl)
                }else{
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

                        val autonym = userInfo.getInt("autonym")
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
            if(PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
        }

        app_bar?.addOnOffsetChangedListener(mAppBarOffsetChangeListener)
    }

    private val mAppBarOffsetChangeListener = object : AppBarStateChangeListener(){

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


}