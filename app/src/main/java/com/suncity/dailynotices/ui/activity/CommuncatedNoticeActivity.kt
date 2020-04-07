package com.suncity.dailynotices.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Notice
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.CommuncatedAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      CommuncatedNotice
 * @Description:    沟通过的通告
 * @UpdateDate:     11/7/2019
 */
class CommuncatedNoticeActivity : BaseActivity() {

    private var communcatedAdapter: CommuncatedAdapter? = null
    companion object {
        private val errorServer = Config.getString(R.string.str_error_server)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true,0f)
            .init()

    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_mine_focus
    }

    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_title_comm_notice)
        communcatedAdapter = CommuncatedAdapter(this@CommuncatedNoticeActivity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this@CommuncatedNoticeActivity)
        recyclerView?.adapter = communcatedAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            finish()
        }

        smartRefreshLayout?.setOnRefreshListener {
            queryRecentNotice(it)
        }

        communcatedAdapter?.setOnItemClickListener(mNoticeItemClick)
    }

    private fun queryRecentNotice(it: RefreshLayout) {

        val objectId = PreferenceStorage.userObjectId
        Query.queryRecentNoticeByObjectId(objectId){ noticeList, avException ->
            it.finishRefresh()
            if (avException == null && (noticeList?.size ?: 0) > 0) {
                val sortedList = arrayListOf<Notice>()
                sortedList.addAll(noticeList!!.sortedWith(kotlin.Comparator { o1, o2 ->
                    val time01 = o1.endTime ?: ""
                    val time02 = o2.endTime ?: ""
                    val isGreater = DateUtils.compareDate(time01,time02)
                    if(isGreater) -1 else 1
                }))
                communcatedAdapter?.clear()
                communcatedAdapter?.addAll(sortedList)
            } else {
                if (avException != null){
                    ToastUtils.showSafeToast(this@CommuncatedNoticeActivity, errorServer)
                }
            }

            if((communcatedAdapter?.itemCount ?: 0) > 0){
                layout_empty?.visibility = View.GONE
            }else{
                layout_empty?.visibility = View.VISIBLE
            }
        }
    }

    private val mNoticeItemClick = object : RecyclerArrayAdapter.OnItemClickListener{

        override fun onItemClick(position: Int, view: View) {
            //TODO 进入通告详情页
            LogUtils.e("进入通告详情页")
        }

    }
}