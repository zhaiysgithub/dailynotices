package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.activity.PushDynamicActivity.Companion.TAG_ADD
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      PushDynamicImgAdapter
 * @Description:     作用描述
 * @UpdateDate:     22/7/2019
 */
class PushDynamicImgAdapter(context: Context) : RecyclerArrayAdapter<String>(context) {

    private val TYPE_ADD_PIC = 1
    private val TYPE_SELE_PIC = 0

    override fun getViewType(position: Int): Int {
        val value = getItem(position)

        return if (value == null || value == TAG_ADD) {
            TYPE_ADD_PIC
        } else {
            TYPE_SELE_PIC
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<String> {
        return when (viewType) {
            TYPE_ADD_PIC -> {
                AddPicViewHolder(parent, R.layout.adapter_item_add_pic)
            }
            TYPE_SELE_PIC -> {
                SelPicViewHolder(parent, R.layout.adapter_item_sel_pic)
            }
            else -> {
                SelPicViewHolder(parent, R.layout.adapter_item_sel_pic)
            }
        }

    }


    inner class AddPicViewHolder(parent: ViewGroup, reslayoutId: Int) : HAFViewHolder<String>(parent, reslayoutId) {

        override fun setData(data: String) {

            itemView.setOnClickListener {
                if (mListener != null) {
                    mListener?.onAddPicClick()
                }
            }
        }
    }

    inner class SelPicViewHolder(parent: ViewGroup, reslayoutId: Int) : HAFViewHolder<String>(parent, reslayoutId) {

        private var pic: SimpleDraweeView? = null

        init {
            pic = itemView.findViewById(R.id.iv_pic)
        }

        override fun setData(data: String) {
            pic?.setImageURI("file://$data")
            itemView.setOnClickListener {
                if (mListener != null) {
                    mListener?.onSelItemClick(data)
                }
            }
        }
    }

    private var mListener: OnPushDynamicImgClickListener? = null

    open fun SetOnPushDynamicImgClickListener(l: OnPushDynamicImgClickListener) {
        mListener = l
    }

    interface OnPushDynamicImgClickListener {

        fun onAddPicClick()

        fun onSelItemClick(url: String)
    }
}