package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.OnDynamicItemMenuClick
import com.suncity.dailynotices.model.Dynamic
import com.suncity.dailynotices.ui.views.flowlayout.FlowLayout
import com.suncity.dailynotices.ui.views.flowlayout.TagAdapter
import com.suncity.dailynotices.ui.views.flowlayout.TagFlowLayout
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import java.util.*
import com.facebook.drawee.generic.RoundingParams
import com.suncity.dailynotices.ui.views.flowlayout.TagView
import com.suncity.dailynotices.utils.*


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.mAdapter
 * @ClassName:      DynamicAdapter
 * @Description:     作用描述
 * @UpdateDate:     12/7/2019
 */
class DynamicAdapter(context: Context) : RecyclerArrayAdapter<Dynamic>(context) {


    companion object {

        private const val TYPE_NO_PIC = 0
        private const val TYPE_PIC_ONE = 1
        private const val TYPE_PIC_TWO = 2
        private const val TYPE_PIC_THREE = 3
        private const val TYPE_PIC_FOUR = 4
        private const val TYPE_PIC_GREATER_FOUR = 5
    }

    private val mInflater = LayoutInflater.from(context)
    private var mMenuClick: OnDynamicItemMenuClick? = null
    private val mContext = context

    override fun getViewType(position: Int): Int {
        val item = getItem(position)
        val size = (item?.images?.size ?: 0)
        return when {
            size == 0 -> TYPE_NO_PIC
            size == 1 -> TYPE_PIC_ONE
            size == 2 -> TYPE_PIC_TWO
            size == 3 -> TYPE_PIC_THREE
            size == 4 -> TYPE_PIC_FOUR
            size >= 5 -> TYPE_PIC_GREATER_FOUR
            else -> TYPE_PIC_GREATER_FOUR
        }
    }


    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<Dynamic> {
        return when (viewType) {
            TYPE_NO_PIC -> {
                DynamicNoPicVH(parent, R.layout.adapter_dynamic_no_pic)
            }
            TYPE_PIC_ONE -> {
                DynamicOnePicVH(parent, R.layout.adapter_dynamic_one_pic)
            }
            TYPE_PIC_TWO -> {
                DynamicTwoPicVH(parent, R.layout.adapter_dynamic_two_pic)
            }
            TYPE_PIC_THREE -> {
                DynamicThreePicVH(parent, R.layout.adapter_dynamic_three_pic)
            }
            TYPE_PIC_FOUR -> {
                DynamicFourPicVH(parent, R.layout.adapter_dynamic_four_pic)
            }
            TYPE_PIC_GREATER_FOUR -> {
                DynamicGreaterFourPicVH(parent, R.layout.adapter_dynamic_greaterfour_pic)
            }
            else -> {
                DynamicGreaterFourPicVH(parent, R.layout.adapter_dynamic_greaterfour_pic)
            }
        }
    }

    inner class DynamicNoPicVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_no)
            tvUsername = itemView.findViewById(R.id.tv_username_no)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_no)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_no)
            ivMore = itemView.findViewById(R.id.iv_more_no)
            tvContent = itemView.findViewById(R.id.tv_content_no)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_no)
            ivZan = itemView.findViewById(R.id.iv_zan_no)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_no)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
        }
    }

    inner class DynamicOnePicVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivPic: SimpleDraweeView? = null
        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_one)
            tvUsername = itemView.findViewById(R.id.tv_username_one)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_one)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_one)
            ivMore = itemView.findViewById(R.id.iv_more_one)
            tvContent = itemView.findViewById(R.id.tv_content_one)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_one)
            ivZan = itemView.findViewById(R.id.iv_zan_one)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_one)
            ivPic = itemView.findViewById(R.id.iv_pic_one_one)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
            setOnePic(ivPic, data)
        }
    }

    inner class DynamicTwoPicVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivPicOne: SimpleDraweeView? = null
        private var ivPicTwo: SimpleDraweeView? = null
        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_two)
            tvUsername = itemView.findViewById(R.id.tv_username_two)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_two)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_two)
            ivMore = itemView.findViewById(R.id.iv_more_two)
            tvContent = itemView.findViewById(R.id.tv_content_two)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_two)
            ivZan = itemView.findViewById(R.id.iv_zan_two)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_two)
            ivPicOne = itemView.findViewById(R.id.iv_pic_one_two)
            ivPicTwo = itemView.findViewById(R.id.iv_pic_two_two)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
            setTwoPic(ivPicOne, ivPicTwo, data)
        }
    }

    inner class DynamicThreePicVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivPicOne: SimpleDraweeView? = null
        private var ivPicTwo: SimpleDraweeView? = null
        private var ivPicThree: SimpleDraweeView? = null
        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_three)
            tvUsername = itemView.findViewById(R.id.tv_username_three)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_three)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_three)
            ivMore = itemView.findViewById(R.id.iv_more_three)
            tvContent = itemView.findViewById(R.id.tv_content_three)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_three)
            ivZan = itemView.findViewById(R.id.iv_zan_three)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_three)
            ivPicOne = itemView.findViewById(R.id.iv_pic_one_three)
            ivPicTwo = itemView.findViewById(R.id.iv_pic_two_three)
            ivPicThree = itemView.findViewById(R.id.iv_pic_three_three)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
            setThreePic(ivPicOne, ivPicTwo, ivPicThree, data)
        }
    }

    inner class DynamicFourPicVH(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivPicOne: SimpleDraweeView? = null
        private var ivPicTwo: SimpleDraweeView? = null
        private var ivPicThree: SimpleDraweeView? = null
        private var ivPicFour: SimpleDraweeView? = null
        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_four)
            tvUsername = itemView.findViewById(R.id.tv_username_four)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_four)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_four)
            ivMore = itemView.findViewById(R.id.iv_more_four)
            tvContent = itemView.findViewById(R.id.tv_content_four)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_four)
            ivZan = itemView.findViewById(R.id.iv_zan_four)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_four)
            ivPicOne = itemView.findViewById(R.id.iv_pic_one_four)
            ivPicTwo = itemView.findViewById(R.id.iv_pic_two_four)
            ivPicThree = itemView.findViewById(R.id.iv_pic_three_four)
            ivPicFour = itemView.findViewById(R.id.iv_pic_four_four)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
            setFourPic(ivPicOne, ivPicTwo, ivPicThree, ivPicFour, data)
        }
    }

    inner class DynamicGreaterFourPicVH(parent: ViewGroup, resLayoutId: Int) :
        HAFViewHolder<Dynamic>(parent, resLayoutId) {

        private var ivAvatar: SimpleDraweeView? = null
        private var tvUsername: TextView? = null
        private var tvCreateDate: TextView? = null
        private var ivAuthMark: ImageView? = null
        private var ivMore: ImageView? = null
        private var tvContent: TextView? = null
        private var tagFlowLayout: TagFlowLayout? = null
        private var ivZan: ImageView? = null
        private var tvZanCount: TextView? = null
        private var picRecyclerView: RecyclerView? = null

        init {
            ivAvatar = itemView.findViewById(R.id.iv_avatar_greaterfour)
            tvUsername = itemView.findViewById(R.id.tv_username_greaterfour)
            tvCreateDate = itemView.findViewById(R.id.tv_createDate_greaterfour)
            ivAuthMark = itemView.findViewById(R.id.iv_auth_mark_greaterfour)
            ivMore = itemView.findViewById(R.id.iv_more_greaterfour)
            tvContent = itemView.findViewById(R.id.tv_content_greaterfour)
            tagFlowLayout = itemView.findViewById(R.id.tagFlowLayout_greaterfour)
            ivZan = itemView.findViewById(R.id.iv_zan_greaterfour)
            tvZanCount = itemView.findViewById(R.id.tv_zan_count_greaterfour)
            picRecyclerView = itemView.findViewById(R.id.pic_recyclerView_greaterfour)
        }

        override fun setData(data: Dynamic) {
            setAvatar(ivAvatar, data)
            setUserName(tvUsername, data)
            setCreateDate(tvCreateDate, data)
            setUserAutonym(ivAuthMark, data)
            setTextContents(tvContent, data)
            setTags(tagFlowLayout, data, adapterPosition)
            setZan(ivZan, tvZanCount, data, adapterPosition)
            setMoreClick(ivMore, adapterPosition)
            setPicRecyclerView(picRecyclerView, data)
        }
    }

    private fun createTagAdapter(stringList: ArrayList<String>): TagAdapter<String> {
        return object : TagAdapter<String>(stringList) {

            override fun getView(parent: FlowLayout, position: Int, t: String): View {

                val view: View = mInflater.inflate(R.layout.tag_item, parent, false)
                val textView = view.findViewById<TextView>(com.suncity.dailynotices.R.id.tv_text)
                textView.text = t
                return view
            }

        }
    }

    private fun setAvatar(avatar: SimpleDraweeView?, data: Dynamic) {
        val avatarurl = data.avatarurl
        if (StringUtils.isNotEmptyAndNull(avatarurl)) {
            avatar?.setImageURI(avatarurl)
            val roundingParams = RoundingParams.fromCornersRadius(4f)
            roundingParams.roundAsCircle = true
            avatar?.hierarchy?.roundingParams = roundingParams
        }
    }

    private fun setUserName(userName: TextView?, data: Dynamic) {
        userName?.text = data.userName
    }

    private fun setCreateDate(createDate: TextView?, data: Dynamic) {
        val createAt = data.createAt
        if (createAt != null) {
            createDate?.text = DateUtils.formatDateToM(createAt)
        }
    }

    private fun setUserAutonym(authMark: ImageView?, data: Dynamic) {
        val userAutonym = data.userAutonym
        authMark?.visibility = if (userAutonym == 1) View.VISIBLE else View.GONE
    }

    private fun setTextContents(content: TextView?, data: Dynamic) {
        content?.text = data.contents
    }

    private fun setTags(tagLayout: TagFlowLayout?, data: Dynamic, pos: Int) {
        val tagList = data.tagList
        if (tagList != null && tagList.size > 0) {
            val tagAdapter = createTagAdapter(tagList)
            tagLayout?.mAdapter = tagAdapter
        }

        tagLayout?.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: TagView, position: Int, parent: FlowLayout): Boolean {
                if (mMenuClick != null && !PreventRepeatedUtils.isFastDoubleClick()) {
                    mMenuClick?.onTagFlowClick(pos, position, (tagList?.get(position) ?: ""))
                }
                return true
            }

        })
    }

    private fun setZan(zan: ImageView?, zanCount: TextView?, data: Dynamic, position: Int) {
        val selected = data.isSelected
        if (selected) {
            zan?.setImageResource(R.mipmap.ico_zan_selected)
        } else {
            zan?.setImageResource(R.mipmap.ico_zan_unselected)
        }
        zanCount?.text = (data.likeNum ?: 0).toString()
        zan?.setOnClickListener {
            if(selected){
                ToastUtils.showSafeToast(mContext,"不能取消点赞")
                return@setOnClickListener
            }else{
                zan.setImageResource(R.mipmap.ico_zan_selected)
                val likeNum = ((data.likeNum ?: 0) + 1)
                zanCount?.text = likeNum.toString()
            }
            if (mMenuClick != null && !PreventRepeatedUtils.isFastDoubleClick()) {
                mMenuClick?.onSelectLikeClick(position,data)
            }
        }
    }

    private fun setMoreClick(ivMore: ImageView?, adapterPosition: Int) {
        ivMore?.setOnClickListener {
            if (mMenuClick != null && !PreventRepeatedUtils.isFastDoubleClick()) {
                mMenuClick?.onMoreClick(adapterPosition)
            }
        }
    }

    private fun setOnePic(ivPic: SimpleDraweeView?, data: Dynamic) {
        val images = data.images
        if (images != null && images.size > 0) {
            ivPic?.setImageURI(images[0])

            setImageClick(ivPic, 0, images[0], data)
        }

    }

    private fun setTwoPic(ivPicOne: SimpleDraweeView?, ivPicTwo: SimpleDraweeView?, data: Dynamic) {
        val images = data.images
        if (images != null && images.size > 0) {
            ivPicOne?.setImageURI(images[0])
            setImageClick(ivPicOne, 0, images[0], data)
            if (images[1].isNotEmpty()) {
                ivPicTwo?.setImageURI(images[1])
                setImageClick(ivPicTwo, 1, images[1], data)
            }
        }
    }

    private fun setThreePic(
        ivPicOne: SimpleDraweeView?, ivPicTwo: SimpleDraweeView?
        , ivPicThree: SimpleDraweeView?, data: Dynamic
    ) {
        val images = data.images
        if (images != null && images.size > 0) {
            ivPicOne?.setImageURI(images[0])
            setImageClick(ivPicOne, 0, images[0], data)
            if (images[1].isNotEmpty()) {
                ivPicTwo?.setImageURI(images[1])
                setImageClick(ivPicTwo, 1, images[1], data)
            }
            if (images[2].isNotEmpty()) {
                ivPicThree?.setImageURI(images[2])
                setImageClick(ivPicThree, 2, images[2], data)
            }
        }
    }

    private fun setFourPic(
        ivPicOne: SimpleDraweeView?, ivPicTwo: SimpleDraweeView?
        , ivPicThree: SimpleDraweeView?, ivPicFour: SimpleDraweeView?, data: Dynamic
    ) {
        val images = data.images
        if (images != null && images.size > 0) {
            ivPicOne?.setImageURI(images[0])
            setImageClick(ivPicOne, 0, images[0], data)
            if (images[1].isNotEmpty()) {
                ivPicTwo?.setImageURI(images[1])
                setImageClick(ivPicTwo, 1, images[1], data)
            }
            if (images[2].isNotEmpty()) {
                ivPicThree?.setImageURI(images[2])
                setImageClick(ivPicThree, 2, images[2], data)
            }
            if (images[3].isNotEmpty()) {
                ivPicFour?.setImageURI(images[3])
                setImageClick(ivPicFour, 3, images[3], data)
            }
        }
    }

    private fun setPicRecyclerView(picRecyclerView: RecyclerView?, data: Dynamic) {
        picRecyclerView?.setHasFixedSize(true)
        picRecyclerView?.layoutManager = GridLayoutManager(mContext, 3)
        picRecyclerView?.isNestedScrollingEnabled = false
        val adapter = DynamicPicAdapter(mContext)
        picRecyclerView?.adapter = adapter
        val images = data.images
        if (images != null && images.size > 0) {
            adapter.addAll(images)
            adapter.setOnItemClickListener(object : OnItemClickListener {

                override fun onItemClick(position: Int, view: View) {
                    val item = adapter.getItem(position) ?: return
                    if (mMenuClick != null) {
                        mMenuClick?.onImageClick(view, position, item, data)
                    }
                }

            })
        }
    }

    private fun setImageClick(imageView: SimpleDraweeView?, pos: Int, url: String, data: Dynamic) {

        imageView?.let { view ->
            view.setOnClickListener {
                if (mMenuClick != null) {
                    mMenuClick?.onImageClick(it, pos, url, data)
                }
            }
        }
    }

    fun setOnDynamicItemMenuClick(itemMenuClick: OnDynamicItemMenuClick) {
        mMenuClick = itemMenuClick
    }


}