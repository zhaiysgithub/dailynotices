package com.suncity.dailynotices.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
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


    private fun queryFireList(it: RefreshLayout) {
        val objectId = PreferenceStorage.userObjectId
        Query.queryFireList(objectId) { firelist, avException ->
            it.finishRefresh()
            if (firelist != null && firelist.size > 0 && avException == null) {
                recommentAdapter?.clear()
                recommentAdapter?.addAll(firelist)
            } else {
                if (avException != null) {
                    ToastUtils.showSafeToast(this@MineRecommentActivity, errorServer)
                }
            }

            if (recommentAdapter?.itemCount == 0) {
                layout_empty?.visibility = View.VISIBLE
            } else {
                layout_empty?.visibility = View.GONE
            }
        }

    }

}