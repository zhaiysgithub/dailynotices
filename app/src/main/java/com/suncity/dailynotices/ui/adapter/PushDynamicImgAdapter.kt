package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.bean.DynamicAdapterBean
import com.suncity.dailynotices.islib.bean.MediaType
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
open class PushDynamicImgAdapter(context: Context) : RecyclerArrayAdapter<DynamicAdapterBean>(context) {

    private val typeAddPic = 1
    private val typeSelePic = 0


    override fun getViewType(position: Int): Int {
        val item = getItem(position)
        val itemPath = item?.path

        return if (itemPath == null || itemPath == TAG_ADD) {
            typeAddPic
        } else {
            typeSelePic
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<DynamicAdapterBean> {
        return when (viewType) {
            typeAddPic -> {
                AddPicViewHolder(parent, R.layout.adapter_item_add_pic)
            }
            typeSelePic -> {
                SelPicViewHolder(parent, R.layout.adapter_item_sel_pic)
            }
            else -> {
                SelPicViewHolder(parent, R.layout.adapter_item_sel_pic)
            }
        }

    }


    inner class AddPicViewHolder(parent: ViewGroup, reslayoutId: Int) :
        HAFViewHolder<DynamicAdapterBean>(parent, reslayoutId) {

        override fun setData(data: DynamicAdapterBean) {

            itemView.setOnClickListener {
                if (mListener != null) {
                    mListener?.onAddPicClick()
                }
            }
        }
    }

    inner class SelPicViewHolder(parent: ViewGroup, reslayoutId: Int) :
        HAFViewHolder<DynamicAdapterBean>(parent, reslayoutId) {

        private var pic: SimpleDraweeView? = null
        private var ivDel: ImageView? = null
        private var ivVideoMark: View? = null

        init {
            pic = itemView.findViewById(R.id.iv_pic)
            ivDel = itemView.findViewById(R.id.iv_del)
            ivVideoMark = itemView.findViewById(R.id.layout_video_mark)
        }

        override fun setData(data: DynamicAdapterBean) {
            val path = data.path
            pic?.setImageURI("file://$path")
            val type = data.type
            if (type == MediaType.TYPE_VIDEO) {
                ivVideoMark?.visibility = View.VISIBLE
            } else {
                ivVideoMark?.visibility = View.GONE
            }
            itemView.setOnClickListener {
                mListener?.onSelItemClick(data)
            }
            ivDel?.setOnClickListener {
                mListener?.onDeleteItem(adapterPosition)
            }
        }
    }

    private var mListener: OnPushDynamicImgClickListener? = null

    open fun setOnPushDynamicImgClickListener(l: OnPushDynamicImgClickListener) {
        mListener = l
    }

    interface OnPushDynamicImgClickListener {

        fun onAddPicClick()

        fun onSelItemClick(data: DynamicAdapterBean)

        fun onDeleteItem(position: Int)
    }
}