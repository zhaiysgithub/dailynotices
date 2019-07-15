package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.dialog.BottomDialogiOSDynamic
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.lcoperation.Increase
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Comments
import com.suncity.dailynotices.model.Dynamic
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.CommentAdapter
import com.suncity.dailynotices.ui.adapter.DynamicPicAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.flowlayout.FlowLayout
import com.suncity.dailynotices.ui.views.flowlayout.TagAdapter
import com.suncity.dailynotices.ui.views.flowlayout.TagFlowLayout
import com.suncity.dailynotices.ui.views.recyclerview.DividerDecoration
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_dynamic_detail.*
import kotlinx.android.synthetic.main.adapter_dynamic_four_pic.*
import kotlinx.android.synthetic.main.adapter_dynamic_greaterfour_pic.*
import kotlinx.android.synthetic.main.adapter_dynamic_no_pic.*
import kotlinx.android.synthetic.main.adapter_dynamic_one_pic.*
import kotlinx.android.synthetic.main.adapter_dynamic_three_pic.*
import kotlinx.android.synthetic.main.adapter_dynamic_two_pic.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      DynamicDetailActivity
 * @Description:     作用描述
 * @UpdateDate:     14/7/2019
 */
class DynamicDetailActivity : BaseActivity() {

    companion object {

        private const val DYNAMIC_EXTRA = "dynamicExtra"
        private val errorServer = Config.getString(R.string.str_error_server)
        private val shieldSuccessTip = Config.getString(R.string.str_shield_success_tips)
        private val IMAGETRANSITION = Config.getString(R.string.image_transition_name)
        private val dividerMargin = DisplayUtils.dip2px(20f)
        private val dividerColor = Config.getColor(R.color.color_f3f3f3)
        private val dividerHeight = Config.getDimension(R.dimen.dp_1).toInt()

        fun start(context: Context, item: Dynamic) {
            val intent = Intent()
            intent.setClass(context, DynamicDetailActivity::class.java)
            intent.putExtra(DYNAMIC_EXTRA, item)
            context.startActivity(intent)
        }
    }

    private var commentAdapter: CommentAdapter? = null
    private var dynamicId: String? = null
    private lateinit var mInflater: LayoutInflater
    private var layoutNoImage = R.layout.adapter_dynamic_no_pic
    private var layoutOneImage = R.layout.adapter_dynamic_one_pic
    private var layoutTwoImage = R.layout.adapter_dynamic_two_pic
    private var layoutThreeImage = R.layout.adapter_dynamic_three_pic
    private var layoutFourImage = R.layout.adapter_dynamic_four_pic
    private var layoutGreaterfourImage = R.layout.adapter_dynamic_greaterfour_pic

    override fun setScreenManager() {
        mInflater = LayoutInflater.from(this)
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .keyboardEnable(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_dynamic_detail
    }

    override fun initData() {
        val dynamicItem = intent.getSerializableExtra(DYNAMIC_EXTRA) as Dynamic
        dynamicId = dynamicItem.objectId
        tv_title_center?.text = Config.getString(R.string.str_dynamic_detail)
        tv_empty_desc?.text = Config.getString(R.string.str_no_comments)
        recyclerView_comments?.isNestedScrollingEnabled = false

        val imgSize = dynamicItem.images?.size ?: 0
        setContainerLayout(imgSize, dynamicItem)
        commentAdapter = CommentAdapter(this)
        recyclerView_comments?.setHasFixedSize(true)
        recyclerView_comments?.layoutManager = LinearLayoutManager(this)
        recyclerView_comments?.addItemDecoration(
            DividerDecoration(
                dividerColor,
                dividerHeight,
                dividerMargin,
                dividerMargin
            )
        )
        recyclerView_comments?.adapter = commentAdapter
        if (StringUtils.isNotEmptyAndNull(dynamicId)) {
            queryDynamicUser(dynamicId!!)
            queryComments(dynamicId!!)
        }


    }

    /**
     * 查询评论数量
     */
    @SuppressLint("SetTextI18n")
    private fun queryComments(dynamicId: String) {
        Query.queryCommentsByDynamicId(dynamicId) { commentsList, avException ->
            if (avException == null && (commentsList?.size ?: 0) > 0) {
                tv_comments_title?.text = "全部${commentsList!!.size}条评论"
                val sortedList = arrayListOf<Comments>()
                sortedList.addAll(commentsList.sortedWith(kotlin.Comparator { o1, o2 ->
                    val time01 = o1.createAt
                    val time02 = o2.createAt
                    val isGreater = DateUtils.compareDate(time01, time02)
                    if (isGreater) -1 else 1
                }))
                commentAdapter?.clear()
                commentAdapter?.addAll(sortedList)
            } else {
                tv_comments_title?.text = Config.getString(R.string.str_dynamic_all)
            }
            if ((commentAdapter?.itemCount ?: 0) > 0) {
                layout_empty?.visibility = View.GONE
            } else {
                layout_empty?.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun queryDynamicUser(dynamicId: String) {

        Query.queryDynamicLikeUserAvatar(dynamicId) { avatarList, e ->
            val size = avatarList?.size ?: 0
            if (size == 0) {
                ll_pie?.visibility = View.GONE
            } else {
                ll_pie?.visibility = View.VISIBLE
                tv_pie_count.text = "${size}个人点了赞"
                setPraises(avatarList!!)
            }

        }
    }

    private fun setPraises(avatarList: MutableList<String>) {
        avatarList.forEach {
            val imageView = mInflater.inflate(R.layout.item_pile_praise, pie_layout, false) as SimpleDraweeView
            imageView.setImageURI(it)
            pie_layout?.addView(imageView)
        }

    }

    private fun setContainerLayout(imgSize: Int, dynamicItem: Dynamic) {
        container_header?.removeAllViews()
        when {
            imgSize == 0 -> {
                val noImageView = mInflater.inflate(layoutNoImage, container_header, false)
                container_header?.addView(noImageView)
                iv_zan_no?.visibility = View.GONE
                tv_zan_count_no?.visibility = View.GONE
                setAvatar(iv_avatar_no, dynamicItem)
                setUserName(tv_username_no, dynamicItem)
                setCreateDate(tv_createDate_no, dynamicItem)
                setUserAutonym(iv_auth_mark_no, dynamicItem)
                setTextContents(tv_content_no, dynamicItem)
                setTags(tagFlowLayout_no, dynamicItem)
                setMoreClick(iv_more_no, dynamicItem.idPointer)
                setLayoutHeaderClick(layout_header_no, dynamicItem.idPointer)
            }
            imgSize == 1 -> {
                val oneImageView = mInflater.inflate(layoutOneImage, container_header, false)
                container_header?.addView(oneImageView)
                iv_zan_one?.visibility = View.GONE
                tv_zan_count_one?.visibility = View.GONE
                setAvatar(iv_avatar_one, dynamicItem)
                setUserName(tv_username_one, dynamicItem)
                setCreateDate(tv_createDate_one, dynamicItem)
                setUserAutonym(iv_auth_mark_one, dynamicItem)
                setTextContents(tv_content_one, dynamicItem)
                setTags(tagFlowLayout_one, dynamicItem)
                setMoreClick(iv_more_one, dynamicItem.idPointer)
                setOnePic(iv_pic_one_one, dynamicItem)
                setLayoutHeaderClick(layout_header_one, dynamicItem.idPointer)
            }
            imgSize == 2 -> {
                val twoImageView = mInflater.inflate(layoutTwoImage, container_header, false)
                container_header?.addView(twoImageView)
                iv_zan_two?.visibility = View.GONE
                tv_zan_count_two?.visibility = View.GONE
                setAvatar(iv_avatar_two, dynamicItem)
                setUserName(tv_username_two, dynamicItem)
                setCreateDate(tv_createDate_two, dynamicItem)
                setUserAutonym(iv_auth_mark_two, dynamicItem)
                setTextContents(tv_content_two, dynamicItem)
                setTags(tagFlowLayout_two, dynamicItem)
                setMoreClick(iv_more_two, dynamicItem.idPointer)
                setTwoPic(iv_pic_one_two, iv_pic_two_two, dynamicItem)
                setLayoutHeaderClick(layout_header_two, dynamicItem.idPointer)
            }
            imgSize == 3 -> {
                val threeImageView = mInflater.inflate(layoutThreeImage, container_header, false)
                container_header?.addView(threeImageView)
                iv_zan_three?.visibility = View.GONE
                tv_zan_count_three?.visibility = View.GONE

                setAvatar(iv_avatar_three, dynamicItem)
                setUserName(tv_username_three, dynamicItem)
                setCreateDate(tv_createDate_three, dynamicItem)
                setUserAutonym(iv_auth_mark_three, dynamicItem)
                setTextContents(tv_content_three, dynamicItem)
                setTags(tagFlowLayout_three, dynamicItem)
                setMoreClick(iv_more_three, dynamicItem.idPointer)
                setThreePic(iv_pic_one_three, iv_pic_two_three, iv_pic_three_three, dynamicItem)
                setLayoutHeaderClick(layout_header_three, dynamicItem.idPointer)
            }
            imgSize == 4 -> {
                val fourImageView = mInflater.inflate(layoutFourImage, container_header, false)
                container_header?.addView(fourImageView)
                iv_zan_four?.visibility = View.GONE
                tv_zan_count_four?.visibility = View.GONE
                setAvatar(iv_avatar_four, dynamicItem)
                setUserName(tv_username_four, dynamicItem)
                setCreateDate(tv_createDate_four, dynamicItem)
                setUserAutonym(iv_auth_mark_four, dynamicItem)
                setTextContents(tv_content_four, dynamicItem)
                setTags(tagFlowLayout_four, dynamicItem)
                setMoreClick(iv_more_four, dynamicItem.idPointer)
                setFourPic(iv_pic_one_four, iv_pic_two_four, iv_pic_three_four, iv_pic_four_four, dynamicItem)
                setLayoutHeaderClick(layout_header_four, dynamicItem.idPointer)
            }
            imgSize >= 5 -> {
                val greaterFourImageView = mInflater.inflate(layoutGreaterfourImage, container_header, false)
                container_header?.addView(greaterFourImageView)
                iv_zan_greaterfour?.visibility = View.GONE
                tv_zan_count_greaterfour?.visibility = View.GONE
                setAvatar(iv_avatar_greaterfour, dynamicItem)
                setUserName(tv_username_greaterfour, dynamicItem)
                setCreateDate(tv_createDate_greaterfour, dynamicItem)
                setUserAutonym(iv_auth_mark_greaterfour, dynamicItem)
                setTextContents(tv_content_greaterfour, dynamicItem)
                setTags(tagFlowLayout_greaterfour, dynamicItem)
                setMoreClick(iv_more_greaterfour, dynamicItem.idPointer)
                setPicRecyclerView(pic_recyclerView_greaterfour, dynamicItem)
                setLayoutHeaderClick(layout_header_greaterfour, dynamicItem.idPointer)
            }
        }
    }

    private fun setLayoutHeaderClick(layout: RelativeLayout?, userObjectId: String?) {
        if (userObjectId == null) return
        layout?.setOnClickListener {
            if (PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
            UserInfoActivity.start(this@DynamicDetailActivity, userObjectId)
        }
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        tv_comment_end?.setOnClickListener {
            if (dynamicId == null) return@setOnClickListener
            val commentContent = et_comment_write?.text?.toString()?.trim()
            if (commentContent?.isEmpty() == true) {
                ToastUtils.showSafeToast(this@DynamicDetailActivity, "请您输入评论内容")
                return@setOnClickListener
            }
            Increase.createComment(dynamicId!!,commentContent!!) {
                if (it == null) {
                    TipDialog.show(this@DynamicDetailActivity, "评论成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH)
                    et_comment_write?.setText("")
                    DisplayUtils.hiddenInputMethod(this@DynamicDetailActivity)
                    //刷新评论列表
                    queryComments(dynamicId!!)
                } else {
                    ToastUtils.showSafeToast(this@DynamicDetailActivity, errorServer)
                }
            }
        }
    }


    private fun createTagAdapter(stringList: ArrayList<String>): TagAdapter<String> {
        return object : TagAdapter<String>(stringList) {

            override fun getView(parent: FlowLayout, position: Int, t: String): View {

                val view = mInflater.inflate(R.layout.tag_item, parent, false)
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

    private fun setTags(tagLayout: TagFlowLayout?, data: Dynamic) {
        val tagList = data.tagList
        if (tagList != null && tagList.size > 0) {
            val tagAdapter = createTagAdapter(tagList)
            tagLayout?.mAdapter = tagAdapter
        }
    }


    private fun setMoreClick(ivMore: ImageView?, idPointer: String?) {
        if (idPointer?.isEmpty() == true) return
        if (!isLogined()) {
            startActivity(LoginActivity::class.java)
        } else {
            ivMore?.setOnClickListener {
                if (!PreventRepeatedUtils.isFastDoubleClick()) {
                    createBottomDialog(idPointer!!)
                }
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
        picRecyclerView?.layoutManager = GridLayoutManager(this, 3)
        picRecyclerView?.isNestedScrollingEnabled = false
        val adapter = DynamicPicAdapter(this)
        picRecyclerView?.adapter = adapter
        val images = data.images
        if (images != null && images.size > 0) {
            adapter.addAll(images)
            adapter.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {

                override fun onItemClick(position: Int, view: View) {
                    val item = adapter.getItem(position) ?: return
                    setImageClick(view, position, item, data)
                }

            })
        }
    }

    private fun createBottomDialog(idPointer: String) {
        val dynamicMoreDialog = BottomDialogiOSDynamic(this)
        dynamicMoreDialog.show()
        dynamicMoreDialog.setClickCallback(object : BottomDialogiOSDynamic.ClickCallback {
            override fun doCancel() {
            }

            override fun doReport() {
                startActivity(ReportActivity::class.java)
            }

            override fun doComplaint() {
                ContactServiceActivity.start(this@DynamicDetailActivity, ContactServiceActivity.TYPE_COMPLAINT)
            }

            override fun doShieldUserclick() {
                Query.queryShieldUser(idPointer) {
                    if (it) {
                        Increase.createShieldToBack(idPointer) { e ->
                            if (e == null) {
                                TipDialog.show(
                                    this@DynamicDetailActivity, shieldSuccessTip
                                    , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH
                                )
                            } else {
                                TipDialog.show(
                                    this@DynamicDetailActivity, errorServer
                                    , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setImageClick(imageView: View?, pos: Int, url: String, data: Dynamic) {
        imageView?.let { view ->
            view.setOnClickListener {
                val images = data.images
                if (images == null || images.size == 0) return@setOnClickListener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val option =
                        ActivityOptions.makeSceneTransitionAnimation(this@DynamicDetailActivity, view, IMAGETRANSITION)
                    val intent = Intent(this@DynamicDetailActivity, ImageViewPagerActivity::class.java)
                    ImageViewPagerActivity.currentPos = pos
                    ImageViewPagerActivity.urls = images
                    startActivity(intent, option.toBundle())
                } else {
                    val intent = Intent(this@DynamicDetailActivity, ImageViewPagerActivity::class.java)
                    val rect = Rect()
                    view.getLocalVisibleRect(rect)
                    intent.sourceBounds = rect
                    ImageViewPagerActivity.currentPos = pos
                    ImageViewPagerActivity.urls = images
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
        }
    }
}