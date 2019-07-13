package com.suncity.dailynotices.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Dynamic
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.adapter.DynamicAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.DividerDecoration
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeDynamicFragment
 * @Description:    动态
 */
class HomeDynamicFragment : BaseFragment() {

    private var dynamicAdapter: DynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalObserverHelper.addObserver(globalObservable)
    }
    companion object {

        private val errorServer = Config.getString(R.string.str_error_server)
        private val dividerMargin = DisplayUtils.dip2px(20f)
        private val dividerColor = Config.getColor(R.color.color_f3f3f3)
        private val dividerHeight = Config.getDimension(R.dimen.dp_1).toInt()
        fun getInstance(): HomeDynamicFragment {
            return HomeDynamicFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home_dynamic
    }

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_dynamic)
        dynamicAdapter = DynamicAdapter(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.addItemDecoration(DividerDecoration(dividerColor,dividerHeight,dividerMargin,dividerMargin))
        recyclerView?.adapter = dynamicAdapter

        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        smartRefreshLayout?.setOnRefreshListener {
            queryDynamicData(it)
        }

        dynamicAdapter?.setOnItemClickListener(mDynamicItemClick)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true, 0f)
                .init()
        }
    }

    /**
     * 1:未登录查询所有的数据
     * 2:已经登录的了，需要过滤 shield 的数据
     * 3:关联的表有 Dynamic , user , shield ,userInfo,like
     */
    private fun queryDynamicData(it: RefreshLayout?) {
        val isLogin = isLogined()
        val userObjectId = PreferenceStorage.userObjectId
        Query.queryDynamicData(isLogin,userObjectId){dynamicList , avException ->
            it?.finishRefresh()
            if (avException == null && (dynamicList?.size ?: 0) > 0) {
                val sortedList = arrayListOf<Dynamic>()
                sortedList.addAll(dynamicList!!.sortedWith(kotlin.Comparator { o1, o2 ->
                    val time01 = o1.createAt
                    val time02 = o2.createAt
                    val isGreater = DateUtils.compareDate(time01,time02)
                    if(isGreater) -1 else 1
                }))
                dynamicAdapter?.clear()
                dynamicAdapter?.addAll(sortedList)
            } else {
                if (avException != null){
                    ToastUtils.showSafeToast(requireContext(), errorServer)
                }
            }

            if((dynamicAdapter?.itemCount ?: 0) > 0){
                layout_empty?.visibility = View.GONE
            }else{
                layout_empty?.visibility = View.VISIBLE
            }

        }
    }

    private val mDynamicItemClick = object : RecyclerArrayAdapter.OnItemClickListener {

        override fun onItemClick(position: Int, view: View) {

        }

    }

    private val globalObservable = object : SimpleGlobalObservable(){

        override fun onLoginSuccess() {
            queryDynamicData(null)
        }

        override fun onLogoutSuccess() {
            queryDynamicData(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObserverHelper.removeObserver(globalObservable)
    }
}