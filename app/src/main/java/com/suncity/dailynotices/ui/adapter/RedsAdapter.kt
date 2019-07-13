package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Cover
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.mAdapter
 * @ClassName:      RedsAdapter
 * @Description:     作用描述
 * @UpdateDate:     12/7/2019
 */
class RedsAdapter(context: Context) : RecyclerArrayAdapter<Cover>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RedsViewHolder {
        return RedsViewHolder(parent, R.layout.adapter_item_reds)
    }

    class RedsViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Cover>(parent, resLayoutId) {

        private var ivRed:SimpleDraweeView? = null
        private var tvDesc:TextView? = null
        private var tvCount:TextView? = null
        init {
            ivRed = itemView.findViewById(R.id.iv_red)
            tvDesc = itemView.findViewById(R.id.tv_desc)
            tvCount = itemView.findViewById(R.id.tv_count)
        }

        override fun setData(data: Cover) {
            ivRed?.setImageURI(data.coverUrl)
            tvDesc?.text = data.userName
            tvCount?.text = (data.fireCount ?: 0).toString()
        }
    }
}