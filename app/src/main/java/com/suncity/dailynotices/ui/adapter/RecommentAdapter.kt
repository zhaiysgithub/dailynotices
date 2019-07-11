package com.suncity.dailynotices.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Fire
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.DateUtils
import com.suncity.dailynotices.utils.PreferenceStorage

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      RecommentAdapter
 * @Description:     作用描述
 * @UpdateDate:     11/7/2019
 */
class RecommentAdapter(context: Context) : RecyclerArrayAdapter<Fire>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecommentViewHolder {

        return RecommentViewHolder(parent, R.layout.adapter_item_recomment)
    }

    class RecommentViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Fire>(parent, resLayoutId) {

        private var avatar: SimpleDraweeView? = null
        private var title: TextView? = null
        private var updateDate: TextView? = null
        private var count: TextView? = null

        init {
            avatar = itemView.findViewById(R.id.iv_avatar)
            title = itemView.findViewById(R.id.tv_title)
            updateDate = itemView.findViewById(R.id.tv_data)
            count = itemView.findViewById(R.id.tv_count)
        }

        @SuppressLint("SetTextI18n")
        override fun setData(data: Fire) {
            val avatarUrl = PreferenceStorage.userAvatar
            avatar?.setImageURI(avatarUrl)
            title?.text = data.reason ?: ""
            val updatedAt = data.updatedAt
            if (updatedAt != null){
                updateDate?.text = DateUtils.formatDateToM(updatedAt)
            }
            count?.text = "+ ${(data.fire ?: 0)}"


        }
    }
}