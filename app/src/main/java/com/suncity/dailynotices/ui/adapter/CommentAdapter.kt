package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Comments
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.DateUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      CommentAdapter
 * @Description:     作用描述
 * @UpdateDate:     15/7/2019
 */
class CommentAdapter(context: Context) : RecyclerArrayAdapter<Comments>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<Comments> {

        return CommentHolder(parent, R.layout.adapter_item_comment)
    }

    class CommentHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Comments>(parent, resLayoutId) {

        private var avatar: SimpleDraweeView? = null
        private var usernameComment: TextView? = null
        private var authMarkComment: ImageView? = null
        private var contentComment: TextView? = null
        private var createDateComment: TextView? = null

        init {
            avatar = itemView.findViewById(R.id.iv_avatar_comment)
            usernameComment = itemView.findViewById(R.id.tv_username_comment)
            authMarkComment = itemView.findViewById(R.id.iv_auth_mark_comment)
            contentComment = itemView.findViewById(R.id.tv_content_comment)
            createDateComment = itemView.findViewById(R.id.tv_createDate_comment)
        }

        override fun setData(data: Comments) {
            avatar?.setImageURI(data.userAvatar)
            usernameComment?.text = data.userName
            contentComment?.text = data.comments
            val createAt = data.createAt
            if (createAt != null) {
                createDateComment?.text = DateUtils.formatDateToM(createAt)
            }

        }
    }
}