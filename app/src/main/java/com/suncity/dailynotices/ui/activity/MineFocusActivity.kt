package com.suncity.dailynotices.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.MineFocusModel
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.RecentVisitAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      MineFocusActivity
 * @Description:     我查看的页面
 * @UpdateDate:     11/7/2019
 */
class MineFocusActivity : BaseActivity() {

    private var focusAdapter: RecentVisitAdapter? = null

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
        tv_title_center?.text = Config.getString(R.string.str_title_fans)
        focusAdapter = RecentVisitAdapter(this@MineFocusActivity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this@MineFocusActivity)
        recyclerView?.adapter = focusAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }


    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

        smartRefreshLayout?.setOnRefreshListener {
            queryRecentVisitUser(it)
        }

        focusAdapter?.setOnItemClickListener(mFocusItemClick)
    }

    private fun queryRecentVisitUser(it: RefreshLayout) {
        val objectId = PreferenceStorage.userObjectId
        //查询 我查看的
        Query.findRecentVisitUserList(objectId) { modelList, avException ->
            it.finishRefresh()
            if (avException == null && (modelList?.size ?: 0) > 0) {
                val sortedList = arrayListOf<MineFocusModel>()
                sortedList.addAll(modelList!!.sortedWith(kotlin.Comparator { o1, o2 ->
                    val data1Crdt = o1.createDate ?: Date()
                    val data2Crdt = o2.createDate ?: Date()
                    if (data2Crdt > data1Crdt) 1 else -1
                }))
                focusAdapter?.clear()
                focusAdapter?.addAll(sortedList)
            } else {
                if(avException != null){
                    ToastUtils.showSafeToast(this@MineFocusActivity, errorServer)
                }
            }

            if((focusAdapter?.itemCount ?: 0) > 0){
                layout_empty?.visibility = View.GONE
            }else{
                layout_empty?.visibility = View.VISIBLE
            }
        }

    }

    private val mFocusItemClick = object : RecyclerArrayAdapter.OnItemClickListener{

        override fun onItemClick(position: Int, view: View) {
            val item = focusAdapter?.getItem(position) ?: return
            val userInfoObjectId = item.userObjcetId ?: return
            if (PreventRepeatedUtils.isFastDoubleClick()) return
            UserInfoActivity.start(this@MineFocusActivity,userInfoObjectId)
        }

    }
}