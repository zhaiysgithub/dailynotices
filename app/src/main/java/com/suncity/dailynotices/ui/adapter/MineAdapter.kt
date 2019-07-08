package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.model.MineModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      MineAdapter
 * @Description:    我的页面的底部的列表模块
 */
class MineAdapter(context: Context) : RecyclerArrayAdapter<MineModel>(context) {


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): MineViewHolder {

        return MineViewHolder(parent, R.layout.adapter_item_mine)
    }

    class MineViewHolder(parent: ViewGroup, resId: Int) : HAFViewHolder<MineModel>(parent, resId) {
        private var ivIcon: ImageView? = null
        private var tvDesc : TextView? = null

        init {
            ivIcon = itemView.findViewById(R.id.iv_item_icon)
            tvDesc = itemView.findViewById(R.id.tv_item_desc)
        }

        override fun setData(data: MineModel) {
            super.setData(data)

            if (data.imageResId != null){
                ivIcon?.setImageResource(data.imageResId!!)
            }
            tvDesc?.text = (data.desc ?: "")

        }
    }
}