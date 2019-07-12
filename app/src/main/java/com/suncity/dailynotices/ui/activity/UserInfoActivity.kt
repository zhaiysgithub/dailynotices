package com.suncity.dailynotices.ui.activity

import android.animation.ArgbEvaluator
import android.content.Context
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.OnApplyWindowInsetsListener
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import com.avos.avoscloud.AVFile
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.AppBarStateChangeListener
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.fragment.UserInfoFragment
import com.suncity.dailynotices.utils.ScreentUtils
import kotlinx.android.synthetic.main.ac_userinfo.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      UserInfoActivity
 * @Description:    用户详情页面
 * @UpdateDate:     11/7/2019
 */
class UserInfoActivity : BaseActivity() {

    private var mAdapter: ViewPagerAdapter? = null
    private var objectId : String? = null
    private val tabTitles = arrayListOf("主页","图片")

    companion object {
        private const val OBJECTID = "objectId"

        fun start(context: Context, objectId: String) {
            val intent = Intent()
            intent.setClass(context, UserInfoActivity::class.java)
            intent.putExtra(OBJECTID, objectId)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_userinfo
    }

    override fun initData() {
        objectId = intent.getStringExtra(OBJECTID)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.ico_nav_back)
        collapsing_toolbar?.isTitleEnabled = false
        collapsing_toolbar?.requestLayout()
        actionBarResponsive()
        mAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager?.offscreenPageLimit = (mAdapter?.count ?: 0)
        viewPager?.adapter = mAdapter
        tabLayout?.setupWithViewPager(viewPager)

        ViewCompat.setOnApplyWindowInsetsListener(view_container, object : OnApplyWindowInsetsListener {

            override fun onApplyWindowInsets(view: View?, insets: WindowInsetsCompat?): WindowInsetsCompat? {

                return insets?.consumeSystemWindowInsets()
            }
        })

        queryData()

    }

    private fun queryData() {
        if (objectId.isNullOrEmpty()) return
        Query.queryUserByObjectId(objectId!!){ avUser,e ->
            if(avUser != null && e == null){
                val username = avUser.username
                toolbar_title?.text = username
                val avatarFile:AVFile = avUser.getAVFile("avatar")
                imageView_header?.setImageURI(avatarFile.url)
            }
        }
    }

    override fun initListener() {

        app_bar?.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
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

        })
    }

    private fun actionBarResponsive() {
        val actionBarHeight = ScreentUtils.getActionBarHeightPixel(this)
        val tabHeight = ScreentUtils.getTabHeight(this)
        if (actionBarHeight > 0) {
            toolbar?.layoutParams?.height = actionBarHeight + tabHeight
            toolbar?.requestLayout()
        }
    }

    private fun setAlphaForView(v: View?, alpha: Float) {
        val alphaAnimation = AlphaAnimation(alpha, alpha)
        alphaAnimation.duration = 0
        alphaAnimation.fillAfter = true
        v?.startAnimation(alphaAnimation)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return UserInfoFragment()
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }
    }
}