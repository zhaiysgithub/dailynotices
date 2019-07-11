package com.suncity.dailynotices.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.avos.avoscloud.AVUser
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Fire
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.RecommentAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      MineRecommentActivity
 * @Description:    推荐我的页面
 * @UpdateDate:     10/7/2019
 */

class MineRecommentActivity : BaseActivity() {

    private val fireList = arrayListOf<Fire>()
    private var recommentAdapter: RecommentAdapter? = null

    companion object {
        private val errorServer = Config.getString(R.string.str_error_server)
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_mine_focus
    }

    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_title_recomment)
        recommentAdapter = RecommentAdapter(this@MineRecommentActivity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this@MineRecommentActivity)
        recyclerView?.adapter = recommentAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }


    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

        smartRefreshLayout?.setOnRefreshListener {
            queryFireList(it)
        }
    }

    private fun queryFireDataFromLocal() {
        val jsonStr = SharedPrefHelper.retireveAny(Constants.SP_KEY_FIRE)
        if (StringUtils.isNotEmptyAndNull(jsonStr)) {
            val gson = Gson()
            val fireObject = gson.fromJson(jsonStr, Fire::class.javaObjectType)
        }
    }

    private fun queryFireList(it: RefreshLayout) {
        val objectId = PreferenceStorage.userObjectId
        Query.queryFireList(objectId) { avObjects, avException ->
            it.finishRefresh()
            if (avObjects != null && avObjects.size > 0 && avException == null) {
                fireList.clear()
                avObjects.forEach {
                    val fire = Fire()
                    val fireCount = it.getInt("fire")
                    val toUser: AVUser = it.getAVUser("toUser")
                    val user: AVUser = it.getAVUser("user")
                    val reason = it.getString("reason")
                    val createData = it.getDate("createdAt")
                    val updateAt = it.getDate("updatedAt")
                    fire.fire = fireCount
                    fire.reason = reason
                    fire.createdAt = createData
                    fire.updatedAt = updateAt
                    fireList.add(fire)
                }
                recommentAdapter?.clear()
                recommentAdapter?.addAll(fireList)

            } else {
                ToastUtils.showSafeToast(this@MineRecommentActivity, errorServer)
            }

            if(recommentAdapter?.itemCount == 0){
                layout_empty?.visibility = View.VISIBLE
            }else{
                layout_empty?.visibility = View.GONE
            }
        }

    }

}