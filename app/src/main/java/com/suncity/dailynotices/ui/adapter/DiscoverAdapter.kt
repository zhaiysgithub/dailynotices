package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Found
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.DateUtils
import com.suncity.dailynotices.utils.StringUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      DiscoverAdapter
 * @Description:     作用描述
 * @UpdateDate:     16/7/2019
 */
class DiscoverAdapter(context: Context) : RecyclerArrayAdapter<Found>(context) {

    private val TYPE_SMALL_PIC = 1
    private val TYPE_BIG_PIC = 2
    override fun getViewType(position: Int): Int {
        return if (position % 3 == 0 && position > 0) {
            TYPE_BIG_PIC
        } else {
            TYPE_SMALL_PIC
        }

    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<Found> {
        return when (viewType) {
            TYPE_SMALL_PIC -> DiscoverySmallPicViewHolder(parent, R.layout.adapter_discovery_small)
            TYPE_BIG_PIC -> DiscoveryBigPicViewHolder(parent, R.layout.adapter_discovery_big)
            else -> DiscoverySmallPicViewHolder(parent, R.layout.adapter_discovery_small)
        }
    }

    class DiscoverySmallPicViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Found>(parent, resLayoutId) {

        private var title: TextView? = null
        private var drawee: SimpleDraweeView? = null
        private var createDate: TextView? = null

        init {
            title = itemView.findViewById(R.id.title_discovery_small)
            drawee = itemView.findViewById(R.id.draweeView_discovery_small)
            createDate = itemView.findViewById(R.id.createDate_discovery_small)
        }

        override fun setData(data: Found) {
            title?.text = data.title
            val contain1 = data.contain1
            if (StringUtils.isEmptyOrNull(contain1)) {
                drawee?.visibility = View.GONE
            } else {
                drawee?.visibility = View.VISIBLE
                drawee?.setImageURI(contain1)
            }
            val createdAt = data.createdAt
            if (createdAt != null) {
                createDate?.visibility = View.VISIBLE
                createDate?.text = DateUtils.formatDateToYMD(createdAt)
            } else {
                createDate?.visibility = View.GONE
            }

        }
    }


    class DiscoveryBigPicViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Found>(parent, resLayoutId) {

        private var title: TextView? = null
        private var drawee: SimpleDraweeView? = null
        private var createDate: TextView? = null

        init {
            title = itemView.findViewById(R.id.title_discovery_big)
            drawee = itemView.findViewById(R.id.draweeView_discovery_big)
            createDate = itemView.findViewById(R.id.createDate_discovery_big)
        }


        override fun setData(data: Found) {
            title?.text = data.title
            val contain1 = data.contain1
            if (StringUtils.isEmptyOrNull(contain1)) {
                drawee?.visibility = View.GONE
            } else {
                drawee?.visibility = View.VISIBLE
                drawee?.setImageURI(contain1)
            }
            val createdAt = data.createdAt
            if (createdAt != null) {
                createDate?.visibility = View.VISIBLE
                createDate?.text = DateUtils.formatDateToYMD(createdAt)
            } else {
                createDate?.visibility = View.GONE
            }

        }
    }
}