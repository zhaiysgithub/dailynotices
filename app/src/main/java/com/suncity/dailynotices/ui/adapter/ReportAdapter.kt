package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.ReportModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      ReportAdapter
 * @Description:     作用描述
 * @UpdateDate:     14/7/2019
 */
class ReportAdapter(context: Context) : RecyclerArrayAdapter<ReportModel>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {

        return ReportViewHolder(parent, R.layout.adapter_item_report)
    }

    class ReportViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<ReportModel>(parent, resLayoutId) {

        private var tvDesc: TextView? = null
        private var ivHook: ImageView? = null

        init {
            tvDesc = itemView.findViewById(R.id.tv_desc)
            ivHook = itemView.findViewById(R.id.iv_hook)
        }

        override fun setData(data: ReportModel) {
            tvDesc?.text = data.strDesc
            if (data.isChecked) {
                ivHook?.visibility = View.VISIBLE
            } else {
                ivHook?.visibility = View.GONE
            }
        }
    }
}