package com.suncity.dailynotices.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Notice
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.DateUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      CommuncatedAdapter
 * @Description:     作用描述
 * @UpdateDate:     11/7/2019
 */
class CommuncatedAdapter(context: Context) : RecyclerArrayAdapter<Notice>(context) {


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): CommuncatedViewHolder {

        return CommuncatedViewHolder(parent, R.layout.adapter_item_notice)
    }

    class CommuncatedViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Notice>(parent, resLayoutId) {

        private var tvNoticeTitle: TextView? = null
        private var tvNoticeMarkPast: TextView? = null
        private var tvNoticeSales: TextView? = null
        private var tvNoticeDesc: TextView? = null
        private var tvNoticeUserinfo: TextView? = null

        init {
            tvNoticeTitle = itemView.findViewById(R.id.tv_notice_title)
            tvNoticeMarkPast = itemView.findViewById(R.id.tv_notice_mark_past)
            tvNoticeSales = itemView.findViewById(R.id.tv_notice_sales)
            tvNoticeDesc = itemView.findViewById(R.id.tv_notice_desc)
            tvNoticeUserinfo = itemView.findViewById(R.id.tv_notice_userinfo)
        }

        @SuppressLint("SetTextI18n")
        override fun setData(data: Notice) {
            tvNoticeTitle?.text = data.title

            tvNoticeUserinfo?.text = "${data.userName} | ${data.endTime}"

            tvNoticeDesc?.text = data.contents
            val endTime = data.endTime ?: ""
            val isPast = DateUtils.compareDate(endTime)
            if(isPast){
                tvNoticeMarkPast?.visibility = View.VISIBLE
                tvNoticeSales?.visibility = View.GONE
            }else{
                tvNoticeMarkPast?.visibility = View.GONE
                tvNoticeSales?.visibility = View.VISIBLE
                tvNoticeSales?.text = "${data.payment}元/人"
            }
        }
    }
}