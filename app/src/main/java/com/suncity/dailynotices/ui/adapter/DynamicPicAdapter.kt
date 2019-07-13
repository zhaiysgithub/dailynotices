package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      DynamicPicAdapter
 * @Description:     作用描述
 * @UpdateDate:     13/7/2019
 */
class DynamicPicAdapter(context: Context) : RecyclerArrayAdapter<String>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<String> {
        return DynamicPicViewHolder(parent, R.layout.adapter_item_dynamic_pic)
    }

    class DynamicPicViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<String>(parent, resLayoutId) {
        private var pic: SimpleDraweeView? = null

        init {
            pic = itemView.findViewById(R.id.iv_pic)
        }

        override fun setData(data: String) {
            pic?.setImageURI(data)
        }
    }
}