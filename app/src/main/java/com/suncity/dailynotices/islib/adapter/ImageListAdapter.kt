package com.suncity.dailynotices.islib.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnImgItemClickListener
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter


class ImageListAdapter(context: Context) : RecyclerArrayAdapter<LocalMedia>(context) {


    private var showCamera: Boolean = false
    private var mutiSelect: Boolean = false
    private var listener: OnImgItemClickListener? = null
    private var mContext: Context = context
    private val typeCamera = 1
    private val typePic = 0
    private val typeVideo = 2

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewType(position: Int): Int {
        return if (position == 0 && showCamera) {
            typeCamera
        } else {
            val itemData = getItem(position)
            if (itemData?.isVideo() == true) {
                typeVideo
            } else {
                typePic
            }

        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<LocalMedia> {
        return when (viewType) {
            typeCamera -> {
                CameraViewHolder(parent, R.layout.is_item_img_sel_take_photo)
            }
            typePic -> {
                PicViewHolder(parent, R.layout.is_item_img_sel)
            }
            typeVideo -> {
                VideoViewHolder(parent, R.layout.is_item_video_sel)
            }
            else -> {
                PicViewHolder(parent, R.layout.is_item_img_sel)
            }
        }

    }


    inner class CameraViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<LocalMedia>(parent, resLayoutId) {

        private var imageView: SimpleDraweeView? = null

        init {
            imageView = itemView.findViewById(R.id.ivTakePhoto)
        }

        override fun setData(data: LocalMedia) {
            imageView?.setBackgroundResource(R.drawable.ic_take_photo)
            imageView?.setOnClickListener {
                if (listener != null)
                    listener?.onImageClick(adapterPosition, data)
            }
        }
    }

    inner class PicViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<LocalMedia>(parent, resLayoutId) {

        private var ivCheckedStatus: ImageView? = null
        private var draweeView: SimpleDraweeView? = null

        init {
            ivCheckedStatus = itemView.findViewById(R.id.ivPhotoCheaked)
            draweeView = itemView.findViewById(R.id.ivImage)
        }

        override fun setData(data: LocalMedia) {
            val filePath = data.path
            if (mutiSelect) {
                ivCheckedStatus?.visibility = View.VISIBLE
                var isContains = Constant.imageList.contains(filePath)
                setCheckedStatus(isContains, ivCheckedStatus)

                ivCheckedStatus?.setOnClickListener {
                    if (listener != null) {
                        val ret = listener?.onCheckedClick(adapterPosition, data)
                        if (ret == 1) { // 局部刷新
                            isContains = Constant.imageList.contains(filePath)
                            setCheckedStatus(isContains, ivCheckedStatus)
                        }
                    }
                }
            } else {
                ivCheckedStatus?.visibility = View.GONE
            }
            itemView.setOnClickListener {
                listener?.onImageClick(adapterPosition, data)
            }
            draweeView?.let {
                ISNav.getInstance().displayImage(mContext, filePath, it)
            }

        }

        private fun setCheckedStatus(isContains: Boolean, ivChecked: ImageView?) {
            ivChecked?.setImageResource(if (isContains) R.drawable.ic_checked else R.drawable.ic_uncheck)
        }
    }

    inner class VideoViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<LocalMedia>(parent, resLayoutId) {

        private var draweeView: SimpleDraweeView? = null

        init {
            draweeView = itemView.findViewById(R.id.ivVideoImage)
        }

        override fun setData(data: LocalMedia) {
            val filePath = data.path

            draweeView?.let {
                ISNav.getInstance().displayImage(mContext, filePath, it)
            }

            itemView.setOnClickListener {
                listener?.onVideoClick(adapterPosition, data)
            }
        }
    }

    fun setShowCamera(showCamera: Boolean) {
        this.showCamera = showCamera
    }

    fun setMutiSelect(mutiSelect: Boolean) {
        this.mutiSelect = mutiSelect
    }

    fun setOnImgItemClickListener(listener: OnImgItemClickListener) {
        this.listener = listener
    }
}
