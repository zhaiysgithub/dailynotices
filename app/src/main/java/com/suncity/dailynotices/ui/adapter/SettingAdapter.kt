package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.model.SettingModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.mAdapter
 * @ClassName:      SettingAdapter
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingAdapter(context: Context) : RecyclerArrayAdapter<SettingModel>(context) {


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): SettingHolder {
        return SettingHolder(parent, R.layout.adapter_item_setting)
    }

    class SettingHolder(parent: ViewGroup, resId: Int) : HAFViewHolder<SettingModel>(parent, resId) {

        private var tvDesc: TextView? = null
        private var tvValue: TextView? = null

        init {
            tvDesc = itemView.findViewById(R.id.tv_desc)
            tvValue = itemView.findViewById(R.id.tv_value)
        }

        override fun setData(data: SettingModel) {
            super.setData(data)

            tvDesc?.text = (data.text_set ?: "")
            tvValue?.text = (data.value_set ?: "")

        }
    }
}