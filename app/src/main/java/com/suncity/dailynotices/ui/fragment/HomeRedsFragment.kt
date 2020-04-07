package com.suncity.dailynotices.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Cover
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.UserInfoActivity
import com.suncity.dailynotices.ui.adapter.RedsAdapter
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeRedsFragment
 * @Description:    红人
 */
class HomeRedsFragment : BaseFragment() {

    private var redsAdapter: RedsAdapter? = null
    companion object {

        private val errorServer = Config.getString(R.string.str_error_server)
        fun getInstance():HomeRedsFragment{
            return HomeRedsFragment()
        }
    }
    override fun setContentView(): Int {
        return R.layout.fg_home_reds
    }

    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_title_comm_notice)
        tv_empty_desc?.text = Config.getString(R.string.str_no_reds)
        redsAdapter = RedsAdapter(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView?.adapter = redsAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        smartRefreshLayout?.setOnRefreshListener {
            queryRecentNotice(it)
        }

        redsAdapter?.setOnItemClickListener(mRedItemClick)
    }

    private fun queryRecentNotice(it: RefreshLayout) {
        Query.queryAllCover{ coverList, avException ->
            it.finishRefresh()
            if (avException == null && (coverList?.size ?: 0) > 0) {
                val sortedList = arrayListOf<Cover>()
                sortedList.addAll(coverList!!.sortedWith(kotlin.Comparator { o1, o2 ->
                    val count1 = o1.fireCount ?: 0
                    val count2 = o2.fireCount ?: 0
                    val isGreater = (count1 > count2)
                    if(isGreater) -1 else 1
                }))
                redsAdapter?.clear()
                redsAdapter?.addAll(sortedList)
            } else {
                if (avException != null){
                    ToastUtils.showSafeToast(requireContext(), errorServer)
                }
            }

            if((redsAdapter?.itemCount ?: 0) > 0){
                layout_empty?.visibility = View.GONE
            }else{
                layout_empty?.visibility = View.VISIBLE
            }
        }

    }

    private val mRedItemClick = object : RecyclerArrayAdapter.OnItemClickListener{

        override fun onItemClick(position: Int, view: View) {
            val item = redsAdapter?.getItem(position)
            val objectId = item?.userObjectId
            if (objectId == null || PreventRepeatedUtils.isFastDoubleClick()) return
            UserInfoActivity.start(requireContext(),objectId,item.coverUrl)
        }

    }
}