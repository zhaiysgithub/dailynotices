package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.DisplayUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      UserInfoPicAdapter
 * @Description:     作用描述
 * @UpdateDate:     15/7/2019
 */
class UserInfoPicAdapter(context: Context) : RecyclerArrayAdapter<String>(context) {

    companion object {
        private var screentWidth = DisplayUtils.getScreenWidth(Config.getApplicationContext())
        private var imageWidth = (screentWidth - DisplayUtils.dip2px(20f) * 2 - DisplayUtils.dip2px(8f)) / 2
    }
    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoPicViewHolder {
        return UserInfoPicViewHolder(parent, R.layout.adapter_item_userinfopic)
    }

    class UserInfoPicViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<String>(parent, resLayoutId) {

        private var image: SimpleDraweeView? = null

        init {
            image = itemView.findViewById(R.id.iv_userinfo_pic)
        }

        override fun setData(data: String) {
            Log.e("@@@","data = $data")
            val layoutParams = image?.layoutParams
            layoutParams?.width = imageWidth
            layoutParams?.height = imageWidth
            image?.layoutParams = layoutParams
            image?.aspectRatio = 1.0f
            image?.setImageURI(data)
        }
    }
}