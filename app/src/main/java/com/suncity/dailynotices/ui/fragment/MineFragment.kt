package com.suncity.dailynotices.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.GetCallback
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.User
import com.suncity.dailynotices.model.UserInfo
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.*
import com.suncity.dailynotices.ui.adapter.MineAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.model.MineModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.fg_mine.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      MineFragment
 * @Description:    我的fragments
 */

class MineFragment : BaseFragment() {

    private var mineAdapter: MineAdapter? = null
    private var dataList: ArrayList<MineModel>? = null

    companion object {
        private val CERTIFIED = Config.getString(R.string.str_certified_name)
        private val NO_CERTIFIED = Config.getString(R.string.str_unreal_name)
        private val GET_USERINFO_ERROR = Config.getString(R.string.str_getuser_error)
        private val authResourceId = R.mipmap.ico_certification
        private val unAuthResourceId = R.mipmap.ico_unreal_name_auth

        fun getInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalObserverHelper.addObserver(mObservable)
    }

    override fun setContentView(): Int {
        return R.layout.fg_mine
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_ffde00)
                .statusBarDarkFont(true, 0f)
                .init()
            initUIData()
        }
    }

    private fun initUIData() {
        Log.e("@@@", "initUIData")
        val isLogin = isLogined()
        changeLoginFlagUi(isLogin)
        if (isLogin) {
            val userJson = SharedPrefHelper.retireveAny(Constants.SP_KEY_USER)
            if (userJson == null) {
                getCurrentUserByLC()
            } else {
                notifyUI(userJson)
            }
        }

        mineAdapter = MineAdapter(requireContext())
        recyclerView_login?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_login?.setHasFixedSize(true)
        recyclerView_login?.adapter = mineAdapter
        mineAdapter?.setOnItemClickListener(mRecyclerViewItemClick)

        val iconArray = arrayListOf(
            R.mipmap.ico_comm_notice, R.mipmap.ico_mine_push
            , R.mipmap.ico_shielding, R.mipmap.ico_service, R.mipmap.ico_about
        )
        val stringArray = Config.getResStringArray(R.array.mine_text)
        dataList = arrayListOf()
        if (iconArray.size == stringArray.size) {
            for (i in 0 until iconArray.size) {
                val model = MineModel(i, iconArray[i], stringArray[i])
                dataList?.add(model)
            }
        }

        mineAdapter?.addAll(dataList)


    }

    private fun notifyUI(json: String) {
        try {
            val userInfoJson = SharedPrefHelper.retireveAny(Constants.SP_KEY_USERINFO)
            val userObjectId = PreferenceStorage.userObjectId
            val gson = Gson()
            val user = gson.fromJson(json, User::class.javaObjectType)
            val userInfo = gson.fromJson(userInfoJson, UserInfo::class.javaObjectType)
            tv_login_user_name?.text = user?.username ?: ""
            PreferenceStorage.userPhoneNum = (user?.mobilePhoneNumber ?: "")
            val avFile = user?.avatar
            val userAvatarUrl = avFile?.url
            PreferenceStorage.userAvatar = (userAvatarUrl ?: "")
            iv_mine_login_avatar?.setImageURI(userAvatarUrl)
            val autonym = userInfo?.autonym ?: 0 //代表是否认证了
            if (autonym == 1) {
                tv_unreal_name_auth.text = CERTIFIED
                iv_unreal_name_auth.setImageResource(authResourceId)
            } else {
                tv_unreal_name_auth.text = NO_CERTIFIED
                iv_unreal_name_auth.setImageResource(unAuthResourceId)
            }
            //设置推荐我的，我查看的，查看我的，RecentVisit这个表中是查看我和我查看的，
            // 推荐我的在Fire中
            Query.queryFire(userObjectId) { fire, e ->
                if (e == null) {
                    val count = fire?.fire ?: 0
                    tv_login_focus_count?.text = formartCount(count) //推荐我的
                }
            }
            //我查看的
            Query.queryRecentVisitUser(userObjectId) { count, e ->
                if (e == null) {

                    tv_login_fans_count?.text = formartCount(count) //我查看的
                }
            }
            //查看我的
            Query.queryRecentVisitVisit(userObjectId) { count, e ->
                if (e == null) {
                    tv_login_guest_count?.text = formartCount(count) //查看我的
                }
            }
        } catch (e: Exception) {
            LogUtils.e("notifyUI -> exception = $e")
        }
    }

    private fun formartCount(count: Int): String {
        return if (count > 999) {
            "+999"
        } else {
            count.toString()
        }
    }

    /**
     * 通过网络获取用户信息
     */
    private fun getCurrentUserByLC() {
        val objectId = PreferenceStorage.userObjectId
        AVUser.getQuery().getInBackground(objectId, object : GetCallback<AVUser>() {
            override fun done(o: AVUser?, e: AVException?) {
                if (o != null && e == null) {
                    SharedPrefHelper.saveAny(Constants.SP_KEY_USER, o)
                    setUIData()
                } else {
                    ToastUtils.showSafeToast(requireContext(), GET_USERINFO_ERROR)
                }
            }

        })
    }

    private fun setUIData() {
        val jsonStr = SharedPrefHelper.retireveAny(Constants.SP_KEY_USER)
        if (jsonStr != null) {
            notifyUI(jsonStr)
        }
    }

    override fun initListener() {
        layout_unlogin?.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }
        layout_login?.setOnClickListener {
            //进入个人主页
            val userObjectId = PreferenceStorage.userObjectId
            if (StringUtils.isEmpty(userObjectId)) return@setOnClickListener
            UserInfoActivity.start(requireContext(),userObjectId)
        }
        iv_mine_tool?.setOnClickListener {
            //进入账号管理页面
            startActivity(SettingActivity::class.java)
        }
        layout_login_focus?.setOnClickListener {
            if (isLogined()) {
                //进入推荐我的页面
                startActivity(MineRecommentActivity::class.java)
            } else {
                startActivity(LoginActivity::class.java)
            }
        }
        layout_login_fans?.setOnClickListener {
            if (isLogined()) {
                //进入我查看的页面
                startActivity(MineFocusActivity::class.java)
            } else {
                startActivity(LoginActivity::class.java)
            }
        }
        layout_login_guest?.setOnClickListener {
            if (isLogined()) {
                //进入查看我的页面
                startActivity(MineFansActivity::class.java)
            } else {
                startActivity(LoginActivity::class.java)
            }
        }

    }

    private val mRecyclerViewItemClick = object : RecyclerArrayAdapter.OnItemClickListener {

        override fun onItemClick(position: Int, view: View) {
            when (position) {
                0 -> {
                    // 沟通过的通告
                    if (!isLogined()) {
                        startActivity(LoginActivity::class.java)
                    } else {
                        startActivity(CommuncatedNoticeActivity::class.java)
                    }
                }
                1 -> {
                    // 我发布的
                    if (!isLogined()) {
                        startActivity(LoginActivity::class.java)
                    } else {
                        val userObjectId = PreferenceStorage.userObjectId
                        if (StringUtils.isEmpty(userObjectId)) return
                        UserInfoActivity.start(requireContext(),userObjectId)
                    }
                }
                2 -> {
                    // 屏蔽过的人
                    startActivity(ShieldingActivity::class.java)
                }
                3 -> {
                    //联系客服
                    ContactServiceActivity.start(requireContext(),ContactServiceActivity.TYPE_CONTACTSERVICE)
                }
                4 -> {
                    //关于我们
                    startActivity(AboutAppActivity::class.java)
                }

            }
        }

    }

    private fun changeLoginFlagUi(isLogin: Boolean) {
        if (isLogin) {
            layout_unlogin?.visibility = View.GONE
            iv_mine_tool?.visibility = View.VISIBLE
            layout_login?.visibility = View.VISIBLE
        } else {
            layout_unlogin?.visibility = View.VISIBLE
            iv_mine_tool?.visibility = View.GONE
            layout_login?.visibility = View.GONE

            tv_login_focus_count?.text = "0"
            tv_login_fans_count?.text = "0"
            tv_login_guest_count?.text = "0"
        }
    }

    private val mObservable = object : SimpleGlobalObservable() {

        override fun onLogoutSuccess() {
            initUIData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObserverHelper.removeObserver(mObservable)
    }
}