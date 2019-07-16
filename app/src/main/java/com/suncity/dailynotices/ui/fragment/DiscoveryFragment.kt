package com.suncity.dailynotices.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.internal.bind.ObjectTypeAdapter
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.WebActivity
import com.suncity.dailynotices.ui.adapter.DiscoverAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.fg_discovery.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      DiscoveryFragment
 * @Description:    发现的fragments
 */

class DiscoveryFragment : BaseFragment() {

    private var discoveryAdapter: DiscoverAdapter? = null
    private val errorServer = Config.getString(R.string.str_error_server)

    companion object {
        fun getInstance(): DiscoveryFragment {
            return DiscoveryFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_discovery
    }

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_data)
        tv_title_center?.text = Config.getString(R.string.str_title_discovery)
        fl_title_back?.visibility = View.GONE

        discoveryAdapter = DiscoverAdapter(requireContext())
        recyclerView_discovery?.setHasFixedSize(true)
        recyclerView_discovery?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_discovery?.adapter = discoveryAdapter

        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {
        smartRefreshLayout?.setOnRefreshListener {
            Query.queryFoundTable { foundList, avException ->
                it.finishRefresh()
                if (foundList != null && foundList.size > 0) {
                    discoveryAdapter?.clear()
                    discoveryAdapter?.addAll(foundList)
                } else {
                    if (avException != null) {
                        ToastUtils.showSafeToast(requireContext(), errorServer)
                    }
                }
                if ((discoveryAdapter?.itemCount ?: 0) > 0) {
                    layout_empty?.visibility = View.GONE
                } else {
                    layout_empty?.visibility = View.VISIBLE
                }
            }
        }

        discoveryAdapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener{

            override fun onItemClick(position: Int, view: View) {
                if(PreventRepeatedUtils.isFastDoubleClick())return
                val foundItem = discoveryAdapter?.getItem(position) ?: return
                WebActivity.start(requireContext(),foundItem.string,null,foundItem.title)
            }

        })

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

}