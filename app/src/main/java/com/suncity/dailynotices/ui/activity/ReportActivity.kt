package com.suncity.dailynotices.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suncity.dailynotices.R
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.model.ReportModel
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.ReportAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.DividerDecoration
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils
import kotlinx.android.synthetic.main.ac_report.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      ReportActivity
 * @Description:     作用描述
 * @UpdateDate:     14/7/2019
 */
class ReportActivity : BaseActivity() {

    companion object {
        private val dividerColor = Config.getColor(R.color.color_999)
        private val dividerPadding = Config.getDimension(R.dimen.dp_20).toInt()
    }

    private var adapter: ReportAdapter? = null
    private val selectedList = arrayListOf<ReportModel>()

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_report
    }

    override fun initData() {
        selectedList.clear()
        tv_title_center.text = Config.getString(R.string.str_report)
        val reportList = setReportData()
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.addItemDecoration(DividerDecoration(dividerColor, 1, dividerPadding, dividerPadding))
        adapter = ReportAdapter(this)
        recyclerView?.adapter = adapter
        adapter?.addAll(reportList)
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        adapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val item = adapter?.getItem(position) ?: return
                val isChecked = item.isChecked
                val selected = !isChecked
                item.isChecked = selected
                adapter?.notifyItemChanged(position)
                if (selected && !selectedList.contains(item)) {
                    selectedList.add(item)
                } else {
                    if (selectedList.contains(item)) {
                        selectedList.remove(item)
                    }
                }
            }
        })

        tv_submit?.setOnClickListener {
            LogUtils.e("selectedList = $selectedList")
            TipDialog.show(this,Config.getString(R.string.str_report_success)
                ,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_FINISH)
            finish()
        }
    }

    private fun setReportData(): ArrayList<ReportModel> {
        val datas = arrayListOf<ReportModel>()
        datas.add(ReportModel("色情低俗", false))
        datas.add(ReportModel("广告骚扰", false))
        datas.add(ReportModel("政治敏感", false))
        datas.add(ReportModel("谣言", false))
        datas.add(ReportModel("欺诈骗钱", false))
        datas.add(ReportModel("违法 (暴力恐怖、违禁品等)", false))
        datas.add(ReportModel("其他", false))
        return datas
    }
}