package com.suncity.dailynotices.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.User
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.AboutAppActivity
import com.suncity.dailynotices.ui.activity.ContactServiceActivity
import com.suncity.dailynotices.ui.activity.LoginActivity
import com.suncity.dailynotices.ui.activity.ShieldingActivity
import com.suncity.dailynotices.ui.adapter.MineAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.model.MineModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.SharedPrefHelper
import kotlinx.android.synthetic.main.fg_mine.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      MineFragment
 * @Description:    我的fragments
 */

class MineFragment : BaseFragment() {

    private var mineAdapter : MineAdapter? = null
    private var dataList : ArrayList<MineModel>? = null
    companion object {
        private val CERTIFIED = Config.getString(R.string.str_certified_name)
        private val NO_CERTIFIED = Config.getString(R.string.str_unreal_name)

        fun getInstance():MineFragment{
            return MineFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_mine
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_ffde00)
                .statusBarDarkFont(true,0f)
                .init()
            initData()
        }
    }

    override fun initData() {
        val isLogin = isLogined()
        changeLoginFlagUi(isLogin)
        if(isLogin){
            val userJson = SharedPrefHelper.retireveAny(Constants.SP_KEY_USER)
            val gson = Gson()
            val user = gson.fromJson(userJson,User::class.java)
            tv_login_user_name?.text = user?.username ?: ""
            val avFile = user?.avatar
            val userInfo = user?.info
            val userAvatarUrl = avFile?.url
            iv_mine_login_avatar?.setImageURI(userAvatarUrl)
            val autonym = userInfo?.autonym ?: 0 //代表是否认证了
            if (autonym == 1)tv_unreal_name_auth.text = CERTIFIED else tv_unreal_name_auth.text = NO_CERTIFIED
            //RecentVisit这个表中是查看我和我查看的，只是selecet字段不同  推荐我的在Fire中
            // TODO

        }

        mineAdapter = MineAdapter(requireContext())
        recyclerView_login?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_login?.setHasFixedSize(true)
        recyclerView_login?.adapter = mineAdapter

        val iconArray = arrayListOf(R.mipmap.ico_comm_notice,R.mipmap.ico_mine_push
            ,R.mipmap.ico_shielding,R.mipmap.ico_service,R.mipmap.ico_about)
        val stringArray = Config.getResStringArray(R.array.mine_text)
        dataList = arrayListOf()
        if (iconArray.size == stringArray.size){
            for (i in 0 until iconArray.size){
                val model = MineModel(i,iconArray[i], stringArray[i])
                dataList?.add(model)
            }
        }

        mineAdapter?.addAll(dataList)


    }

    override fun initListener() {
        layout_unlogin?.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }
        layout_login?.setOnClickListener {
            //进入个人主页
        }
        iv_mine_tool?.setOnClickListener {
            //进入账号管理页面
            LogUtils.e("进入账号管理页面")
        }
        layout_login_focus?.setOnClickListener {
            if (isLogined()){
                //进入推荐我的页面
                LogUtils.e("进入推荐我的页面")
            }else{
                startActivity(LoginActivity::class.java)
            }
        }
        layout_login_fans?.setOnClickListener {
            if (isLogined()){
                //进入我查看的页面
                LogUtils.e("进入我查看的页面")
            }else{
                startActivity(LoginActivity::class.java)
            }
        }
        layout_login_guest?.setOnClickListener {
            if (isLogined()){
                //进入查看我的页面
                LogUtils.e("进入查看我的页面")
            }else{
                startActivity(LoginActivity::class.java)
            }
        }
        mineAdapter?.setOnItemClickListener(mRecyclerViewItemClick)
    }

    private val mRecyclerViewItemClick = object : RecyclerArrayAdapter.OnItemClickListener{

        override fun onItemClick(position: Int, view: View) {
            when(position){
                0 -> {
                    // 沟通过的通告
                    if(!isLogined()){
                        startActivity(LoginActivity::class.java)
                    }else{
                        LogUtils.e((dataList?.get(position)?.desc?:""))
                    }
                }
                1 -> {
                    // 我发布的
                    if(!isLogined()){
                        startActivity(LoginActivity::class.java)
                    }else{
                        LogUtils.e((dataList?.get(position)?.desc?:""))
                    }
                }
                2 -> {
                    // 屏蔽过的人
                    startActivity(ShieldingActivity::class.java)
                }
                3 -> {
                    //联系客服
                    startActivity(ContactServiceActivity::class.java)
                }
                4 -> {
                    //关于我们
                    startActivity(AboutAppActivity::class.java)
                }

            }
        }

    }

    private fun changeLoginFlagUi(isLogin:Boolean){
        if (isLogin){
            layout_unlogin?.visibility = View.GONE
            iv_mine_tool?.visibility = View.VISIBLE
            layout_login?.visibility = View.VISIBLE
        }else{
            layout_unlogin?.visibility = View.VISIBLE
            iv_mine_tool?.visibility = View.GONE
            layout_login?.visibility = View.GONE
        }
    }
}