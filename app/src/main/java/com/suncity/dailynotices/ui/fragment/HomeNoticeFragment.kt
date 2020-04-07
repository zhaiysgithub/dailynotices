package com.suncity.dailynotices.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Notice
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.NoticeDetailActivity
import com.suncity.dailynotices.ui.adapter.CommuncatedAdapter
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.DateUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeNoticeFragment
 * @Description:    通告
 */

class HomeNoticeFragment : BaseFragment() {

    private var communcatedAdapter: CommuncatedAdapter? = null


    companion object {

        private val errorServer = Config.getString(R.string.str_error_server)
        fun getInstance():HomeNoticeFragment{
            return HomeNoticeFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home_notice
    }

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_notice)
        communcatedAdapter = CommuncatedAdapter(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = communcatedAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        smartRefreshLayout?.setOnRefreshListener {
            queryRecentNotice(it)
        }

        communcatedAdapter?.setOnItemClickListener(mNoticeItemClick)
    }

    private fun queryRecentNotice(it: RefreshLayout) {
        Query.queryAllRecentNotice{ noticeList, avException ->
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
                    ToastUtils.showSafeToast(requireContext(), errorServer)
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
            val item = communcatedAdapter?.getItem(position)?:return
            NoticeDetailActivity.start(requireContext(),item)
        }

    }

}