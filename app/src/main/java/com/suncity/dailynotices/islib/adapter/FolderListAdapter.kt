package com.suncity.dailynotices.islib.adapter

import android.content.Context
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.yuyh.easyadapter.abslistview.EasyLVAdapter
import com.yuyh.easyadapter.abslistview.EasyLVHolder
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.bean.MediaLocalInfo
import com.suncity.dailynotices.islib.common.OnFolderChangeListener


class FolderListAdapter(
    private val context: Context,
    private val folderList: MutableList<MediaLocalInfo>
) : EasyLVAdapter<MediaLocalInfo>(context, folderList, R.layout.is_item_img_sel_folder) {

    var selectIndex = 0
        set(position) {
            if (selectIndex == position)
                return
            if (listener != null)
                listener?.onChange(position, folderList[position])
            field = position
            notifyDataSetChanged()
        }
    private var listener: OnFolderChangeListener? = null

    private val totalImageSize: Int
        get() {
            var result = 0
            if (folderList.size > 0) {
                for (folder in folderList) {
                    result += folder.localMedias.size
                }
            }
            return result
        }

    override fun convert(holder: EasyLVHolder, position: Int, folder: MediaLocalInfo) {
        if (position == 0) {
            holder.setText(R.id.tvFolderName, "所有图片")
                .setText(R.id.tvImageNum, "共" + totalImageSize + "张")
            val ivFolder = holder.getView<SimpleDraweeView>(R.id.ivFolder)
            if (folderList.size > 0) {
                ISNav.getInstance().displayImage(context, folder.cover, ivFolder)
            }
        } else {
            holder.setText(R.id.tvFolderName, folder.parentPath)
                .setText(R.id.tvImageNum, "共" + folder.localMedias.size + "张")
            val ivFolder = holder.getView<SimpleDraweeView>(R.id.ivFolder)
            if (folderList.size > 0) {
                ISNav.getInstance().displayImage(context, folder.cover, ivFolder)
            }
        }

        holder.setVisible(R.id.viewLine, position != count - 1)

        if (selectIndex == position) {
            holder.setVisible(R.id.indicator, true)
        } else {
            holder.setVisible(R.id.indicator, false)
        }

        holder.convertView.setOnClickListener { _ -> selectIndex = position }
    }

    fun setData(folders: List<MediaLocalInfo>?) {
        folderList.clear()
        if (folders != null && folders.isNotEmpty()) {
            folderList.addAll(folders)
        }
        notifyDataSetChanged()
    }

    fun setOnFloderChangeListener(listener: OnFolderChangeListener) {
        this.listener = listener
    }
}
