package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Shield
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.PreventRepeatedUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      ShieldAdapter
 * @Description:     作用描述
 * @UpdateDate:     11/7/2019
 */
class ShieldAdapter(context: Context) : RecyclerArrayAdapter<Shield>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): ShieldViewHolder {

        return ShieldViewHolder(parent, R.layout.adapter_item_shield)
    }

    inner class ShieldViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Shield>(parent, resLayoutId) {

        private var ivShieldAvatar: SimpleDraweeView? = null
        private var tvShieldUsername: TextView? = null
        private var tvShieldCate: TextView? = null
        private var tvShieldRemove: TextView? = null
        private var ivShieldMarkAuth: ImageView? = null

        init {
            ivShieldAvatar = itemView.findViewById(R.id.iv_shield_avatar)
            tvShieldUsername = itemView.findViewById(R.id.tv_shield_username)
            tvShieldCate = itemView.findViewById(R.id.tv_shield_cate)
            tvShieldRemove = itemView.findViewById(R.id.tv_shield_remove)
            ivShieldMarkAuth = itemView.findViewById(R.id.iv_shield_mark_auth)
        }

        override fun setData(data: Shield) {
            ivShieldAvatar?.setImageURI(data.avatarurl)
            tvShieldUsername?.text = data.shieldUserName
            tvShieldCate?.text = data.userCategory
            tvShieldRemove?.setOnClickListener {
                if (listener != null && !PreventRepeatedUtils.isFastDoubleClick()) {
                    listener!!.onItemRemoved(data)
                }
            }
            val authCode = data.autonym
            ivShieldMarkAuth?.visibility = if(authCode == 1) View.VISIBLE else View.GONE
        }
    }

    private var listener: OnShieldItemRemoveListener? = null

    fun setOnShieldItemRemoveListener(l: OnShieldItemRemoveListener) {
        listener = l
    }

    interface OnShieldItemRemoveListener {

        fun onItemRemoved(data: Shield)
    }
}