package com.suncity.dailynotices.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.lcoperation.Query
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
        val isLogin = isLogined()
        changeLoginFlagUi(isLogin)
        if (isLogin) {
            notifyUI()
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

    private fun notifyUI() {
        try {
            val userObjectId = PreferenceStorage.userObjectId
            tv_login_user_name?.text = PreferenceStorage.userName
            iv_mine_login_avatar?.setImageURI(PreferenceStorage.userAvatar)
            Query.queryAutonym(userObjectId) { isAutonym, e ->
                if (isAutonym) {
                    tv_unreal_name_auth.text = CERTIFIED
                    iv_unreal_name_auth.setImageResource(authResourceId)
                } else {
                    tv_unreal_name_auth.text = NO_CERTIFIED
                    iv_unreal_name_auth.setImageResource(unAuthResourceId)
                }
            }
            //设置推荐我的，我查看的，查看我的，RecentVisit这个表中是查看我和我查看的，
            // 推荐我的在Fire中
            notifyFire(userObjectId)
            //我查看的
            notifyRencentVisitUser(userObjectId)
            //查看我的
            notifyRecentVisitVisit(userObjectId)
        } catch (e: Exception) {
            LogUtils.e("notifyUI -> exception = $e")
        }
    }

    /**
     * 更新fire数据
     */
    private fun notifyFire(objectid: String) {
        Query.queryFire(objectid) { fire, e ->
            if (e == null) {
                val count = fire?.fire ?: 0
                tv_login_focus_count?.text = formartCount(count) //推荐我的
            }
        }
    }

    /**
     * 更新我查看的
     */
    private fun notifyRencentVisitUser(objectid: String) {
        Query.queryRecentVisitUser(objectid) { count, e ->
            if (e == null) {

                tv_login_fans_count?.text = formartCount(count) //我查看的
            }
        }
    }

    /**
     * 更新查看我的
     */
    private fun notifyRecentVisitVisit(objectid: String) {
        Query.queryRecentVisitVisit(objectid) { count, e ->
            if (e == null) {
                tv_login_guest_count?.text = formartCount(count) //查看我的
            }
        }
    }

    private fun formartCount(count: Int): String {
        return if (count > 999) {
            "+999"
        } else {
            count.toString()
        }
    }


    override fun initListener() {
        layout_unlogin?.setOnClickListener {
            LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
        }
        layout_login?.setOnClickListener {
            //进入个人主页
            val userObjectId = PreferenceStorage.userObjectId
            if (StringUtils.isEmpty(userObjectId)) return@setOnClickListener
            UserInfoActivity.start(requireContext(), userObjectId)
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
                LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
            }
        }
        layout_login_fans?.setOnClickListener {
            if (isLogined()) {
                //进入我查看的页面
                startActivity(MineFocusActivity::class.java)
            } else {
                LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
            }
        }
        layout_login_guest?.setOnClickListener {
            if (isLogined()) {
                //进入查看我的页面
                startActivity(MineFansActivity::class.java)
            } else {
                LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
            }
        }

    }

    private val mRecyclerViewItemClick = object : RecyclerArrayAdapter.OnItemClickListener {

        override fun onItemClick(position: Int, view: View) {
            when (position) {
                0 -> {
                    // 沟通过的通告
                    if (!isLogined()) {
                        LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
                    } else {
                        startActivity(CommuncatedNoticeActivity::class.java)
                    }
                }
                1 -> {
                    // 我发布的
                    if (!isLogined()) {
                        LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
                    } else {
                        val userObjectId = PreferenceStorage.userObjectId
                        if (StringUtils.isEmpty(userObjectId)) return
                        UserInfoActivity.start(requireContext(), userObjectId)
                    }
                }
                2 -> {
                    // 屏蔽过的人
                    if (!isLogined()) {
                        LoginActivity.start(requireContext(), HomeActivity.POS_MINE)
                    } else {
                        startActivity(ShieldingActivity::class.java)
                    }
                }
                3 -> {
                    //联系客服
                    ContactServiceActivity.start(requireContext(), ContactServiceActivity.TYPE_CONTACTSERVICE)
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

        override fun onLoginSuccess() {
            initUIData()
        }

        override fun onUpdateUserinfoSuccess() {
            initUIData()
        }

        override fun onNotifyRecentVisitUser() {
            val myid = PreferenceStorage.userObjectId
            notifyRencentVisitUser(myid)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObserverHelper.removeObserver(mObservable)
    }
}