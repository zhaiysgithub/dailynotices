package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      LinkLeftAdapter
 * @Description:     作用描述
 * @UpdateDate:     22/7/2019
 */
class LinkLeftAdapter(context: Context) : RecyclerArrayAdapter<String>(context) {

    private val textcolor_checked = Config.getColor(R.color.color_222)
    private val textcolor_unchecked = Config.getColor(R.color.color_888)
    private val color_item_checked = Config.getColor(R.color.color_white)
    private val color_item__unchecked = Config.getColor(R.color.color_f7f7f7)
    var checkedPosition: Int = 0

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<String> {

        return LinkLeftViewHolder(parent, R.layout.adapter_item_link_left)
    }


    inner class LinkLeftViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<String>(parent, resLayoutId) {

        private var leftTitle: TextView? = null
        private var ivCheckedMarked: ImageView? = null

        init {
            leftTitle = itemView.findViewById(R.id.tv_left_title)
            ivCheckedMarked = itemView.findViewById(R.id.iv_checked_marked)
        }

        override fun setData(data: String) {
            leftTitle?.text = data
            if (adapterPosition == checkedPosition) {
                leftTitle?.setTextColor(textcolor_checked)
                itemView.setBackgroundColor(color_item_checked)
                ivCheckedMarked?.visibility = View.VISIBLE
            } else {
                leftTitle?.setTextColor(textcolor_unchecked)
                itemView.setBackgroundColor(color_item__unchecked)
                ivCheckedMarked?.visibility = View.GONE
            }
        }
    }
}