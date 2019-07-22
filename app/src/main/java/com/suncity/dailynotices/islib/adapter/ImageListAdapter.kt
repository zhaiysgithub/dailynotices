package com.suncity.dailynotices.islib.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnImgItemClickListener
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter


class ImageListAdapter(context: Context) : RecyclerArrayAdapter<Image>(context) {


    private var showCamera: Boolean = false
    private var mutiSelect: Boolean = false
    private var listener: OnImgItemClickListener? = null
    private var mContext: Context = context
    private val TYPECAMERA = 1
    private val TYPEPIC = 0

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewType(position: Int): Int {
        return if (position == 0 && showCamera) {
            TYPECAMERA
        } else {
            TYPEPIC
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<Image> {
        return when(viewType){
            TYPECAMERA -> {
                CameraViewHolder(parent,R.layout.is_item_img_sel_take_photo)
            }
            TYPEPIC -> {
                PicViewHolder(parent,R.layout.is_item_img_sel)
            }
            else -> {
                PicViewHolder(parent,R.layout.is_item_img_sel)
            }
        }

    }


    inner class CameraViewHolder(parent: ViewGroup,resLayoutId:Int):HAFViewHolder<Image>(parent,resLayoutId){

        private var imageView:SimpleDraweeView? = null
        init {
            imageView = itemView.findViewById(R.id.ivTakePhoto)
        }

        override fun setData(data: Image) {
            imageView?.setBackgroundResource(R.drawable.ic_take_photo)
            imageView?.setOnClickListener {
                if (listener != null)
                    listener?.onImageClick(adapterPosition, data)
            }
        }
    }

    inner class PicViewHolder(parent: ViewGroup,resLayoutId:Int):HAFViewHolder<Image>(parent,resLayoutId){

        private var imageView:ImageView? = null
        private var draweeView:SimpleDraweeView? = null
        init {
            imageView = itemView.findViewById(R.id.ivPhotoCheaked)
            draweeView = itemView.findViewById(R.id.ivImage)
        }
        override fun setData(data: Image) {
            if (mutiSelect){
                imageView?.visibility = View.VISIBLE
                imageView?.setOnClickListener {
                    if (listener != null) {
                        val ret = listener?.onCheckedClick(adapterPosition, data)
                        if (ret == 1) { // 局部刷新
                            if (Constant.imageList.contains(data.path)) {
                                imageView?.setImageResource(R.drawable.ic_checked)
                            } else {
                                imageView?.setImageResource(R.drawable.ic_uncheck)
                            }
                        }
                    }
                }
            }else{
                imageView?.visibility = View.GONE
            }
            itemView.setOnClickListener {
                if (listener != null)
                    listener?.onImageClick(adapterPosition, data)
            }
            if (draweeView != null){
                ISNav.getInstance().displayImage(mContext, data.path, draweeView!!)
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
