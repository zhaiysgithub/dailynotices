package com.suncity.dailynotices.islib.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnItemClickListener
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter
import com.yuyh.easyadapter.recyclerview.EasyRVHolder

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
class ImageListAdapter(private val context: Context, list: List<Image>) :
    EasyRVAdapter<Image>(context, list, R.layout.is_item_img_sel, R.layout.is_item_img_sel_take_photo) {

    private var showCamera: Boolean = false
    private var mutiSelect: Boolean = false
    private var listener: OnItemClickListener? = null

    override fun onBindData(viewHolder: EasyRVHolder, position: Int, item: Image) {

        if (position == 0 && showCamera) {
            val iv = viewHolder.getView<ImageView>(R.id.ivTakePhoto)
            iv.setImageResource(R.drawable.ic_take_photo)
            iv.setOnClickListener {
                if (listener != null)
                    listener?.onImageClick(position, item)
            }
            return
        }

        if (mutiSelect) {
            viewHolder.getView<View>(R.id.ivPhotoCheaked).setOnClickListener {
                if (listener != null) {
                    val ret = listener?.onCheckedClick(position, item)
                    if (ret == 1) { // 局部刷新
                        if (Constant.imageList.contains(item.path)) {
                            viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_checked)
                        } else {
                            viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_uncheck)
                        }
                    }
                }
            }
        }

        viewHolder.setOnItemViewClickListener { v ->
            if (listener != null)
                listener?.onImageClick(position, item)
        }

        val iv = viewHolder.getView<SimpleDraweeView>(R.id.ivImage)
        ISNav.getInstance().displayImage(context, item.path, iv)

        if (mutiSelect) {
            viewHolder.setVisible(R.id.ivPhotoCheaked, true)
            if (Constant.imageList.contains(item.path)) {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_checked)
            } else {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_uncheck)
            }
        } else {
            viewHolder.setVisible(R.id.ivPhotoCheaked, false)
        }
    }

    fun setShowCamera(showCamera: Boolean) {
        this.showCamera = showCamera
    }

    fun setMutiSelect(mutiSelect: Boolean) {
        this.mutiSelect = mutiSelect
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && showCamera) {
            1
        } else 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
