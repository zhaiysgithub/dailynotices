package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.UserInfoRecord
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      RecordAdapter
 * @Description:    个人中心档案的数据
 * @UpdateDate:     16/7/2019
 */
class RecordAdapter(context: Context):RecyclerArrayAdapter<UserInfoRecord>(context) {


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(parent, R.layout.adapter_item_record)
    }


    class RecordViewHolder(parent: ViewGroup,resLayoutId:Int):HAFViewHolder<UserInfoRecord>(parent,resLayoutId){

        private var tvDesc : TextView? = null
        private var tvValue : TextView? = null
        init {
            tvDesc = itemView.findViewById(R.id.tv_desc)
            tvValue = itemView.findViewById(R.id.tv_value)

        }
        override fun setData(data: UserInfoRecord) {

            tvDesc?.text = data.infoDesc
            tvValue?.text = data.indoValue
        }
    }
}