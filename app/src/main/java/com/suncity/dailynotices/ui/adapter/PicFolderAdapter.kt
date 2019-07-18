package com.suncity.dailynotices.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMediaFolder
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      PicFolderAdapter
 * @Description:     作用描述
 * @UpdateDate:     18/7/2019
 */
class PicFolderAdapter(context: Context) : RecyclerArrayAdapter<LocalMediaFolder>(context) {

    private var mimeType: Int = PictureConfig.TYPE_IMAGE

    fun setMimeType(mimeType: Int) {
        this.mimeType = mimeType
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): PicFolderVH {
        return PicFolderVH(parent, R.layout.adapter_item_pic_album_folder)
    }

    inner class PicFolderVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<LocalMediaFolder>(parent, resLayoutId) {

        private var firstImage: SimpleDraweeView? = null
        private var tvFolderName: TextView? = null
        private var imageNum: TextView? = null
        private var tvSign: TextView? = null

        init {
            firstImage = itemView.findViewById(R.id.first_image)
            tvFolderName = itemView.findViewById(R.id.tv_folder_name)
            imageNum = itemView.findViewById(R.id.image_num)
            tvSign = itemView.findViewById(R.id.tv_sign)
        }

        @SuppressLint("SetTextI18n")
        override fun setData(data: LocalMediaFolder) {
            val name = data.name
            val imageNum = data.imageNum
            val imagePath = data.firstImagePath
            val isChecked = data.isChecked
            val checkedNum = data.checkedNum
            itemView.isSelected = isChecked
            tvSign?.visibility = if (checkedNum > 0) View.VISIBLE else View.GONE
            if(mimeType == PictureMimeType.ofAudio()){
                firstImage?.setActualImageResource(R.drawable.audio_placeholder)
            }else{
                firstImage?.setImageURI(imagePath)
            }
            this.imageNum?.text = "($imageNum)"
            tvFolderName?.text = name

        }
    }
}