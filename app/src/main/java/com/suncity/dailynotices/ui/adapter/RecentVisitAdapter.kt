package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.MineFocusModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.DateUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.mAdapter
 * @ClassName:      RecentVisitAdapter
 * @Description:    我查看的和查看我的adapter
 * @UpdateDate:     11/7/2019
 */
class RecentVisitAdapter(context: Context) : RecyclerArrayAdapter<MineFocusModel>(context) {


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecentVisitViewHolder {
        return RecentVisitViewHolder(parent, R.layout.adapter_item_mine_visit)
    }

    class RecentVisitViewHolder(parent: ViewGroup, resLayoutId: Int) :
        HAFViewHolder<MineFocusModel>(parent, resLayoutId) {

        private var avatar: SimpleDraweeView? = null
        private var tvUserName: TextView? = null
        private var tvDate: TextView? = null

        init {
            avatar = itemView.findViewById(R.id.iv_avatar)
            tvUserName = itemView.findViewById(R.id.tv_username)
            tvDate = itemView.findViewById(R.id.tv_date)
        }

        override fun setData(data: MineFocusModel) {
            avatar?.setImageURI(data.userAvatar)
            tvUserName?.text = data.userName
            val updateDate = data.createDate
            if (updateDate != null) {
                val formatDateToM = DateUtils.formatDateToM(updateDate)
                tvDate?.text = formatDateToM
            }
        }
    }
}