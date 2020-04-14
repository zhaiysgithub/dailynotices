package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.dialog.OnDismissListener
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.islib.bean.DynamicAdapterBean
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.bean.MediaType
import com.suncity.dailynotices.islib.common.PublishConstant
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.islib.ui.SimplePlayerActivity
import com.suncity.dailynotices.lcoperation.Increase
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.PushDynamicImgAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.dialog.NormalDialogUtils
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_push_dynamic.*
import java.lang.StringBuilder

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      PushDynamicActivity
 * @Description:    发布动态的页面
 * @UpdateDate:     18/7/2019
 */
class PushDynamicActivity : BaseActivity() {

    private var mAdapter: PushDynamicImgAdapter? = null
    private var selectedSkillList: ArrayList<String>? = null
    private var selectedStyleList: ArrayList<String>? = null
    private var currentVideo: LocalMedia? = null

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    companion object {
        private const val REQUEST_LIST_CODE = 0
        const val TAG_ADD = "addTag"
        const val REQUEST_SKILL_CODE = 2
        const val REQUEST_STYLE_CODE = 3
        const val TYPE_SKILL = "type_skill"
        const val TYPE_STYLE = "type_style"
        const val TYPE_ACTING = "type_acting"
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_push_dynamic
    }

    override fun initData() {
        GlobalObserverHelper.addObserver(mObservable)
        mAdapter = PushDynamicImgAdapter(this)
        recyclerView_selected_pic?.layoutManager = GridLayoutManager(this, 3)
        recyclerView_selected_pic?.addItemDecoration(itemDecoration)
        recyclerView_selected_pic?.adapter = mAdapter
        mAdapter?.add(DynamicAdapterBean(TAG_ADD, MediaType.TYPE_IMAGE))
    }

    private val itemDecoration = object : RecyclerView.ItemDecoration() {
        var spacing = DisplayUtils.dip2px(2f)
        var halfSpacing = spacing shr 1
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = halfSpacing
            outRect.right = halfSpacing
            outRect.top = halfSpacing
            outRect.bottom = halfSpacing
        }
    }

    private val mObservable = object : SimpleGlobalObservable() {

        override fun onVideoSelected(videoMedia: LocalMedia) {
            val allDatas = mAdapter?.getAllData() ?: return
            val addItem = allDatas.find { it.path == TAG_ADD }
            val containAddTag = addItem != null
            val containVideo = allDatas.find { it.type == MediaType.TYPE_VIDEO } != null
            val videoCover = videoMedia.path
            val videoBean = DynamicAdapterBean(videoCover, MediaType.TYPE_VIDEO)
            val size = allDatas.size
            val maxSize = PublishConstant.maxSize
            if (containVideo) {
                val formartStr =
                    String.format(Config.getString(R.string.str_video_maxnum), PublishConstant.videoMaxSize)
                ToastUtils.showSafeToast(this@PushDynamicActivity, formartStr)
            } else {
                if (containAddTag && size >= (maxSize - 1)) { // 8个了
                    addItem?.let {
                        mAdapter?.remove(it)
                    }
                }
                mAdapter?.clear()
                mAdapter?.add(videoBean)
                currentVideo = videoMedia
            }
        }
    }

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            //TODO 弹出dialog 确认是否需要保存记录
            finish()
        }

        title_right_push_dynamic?.setOnClickListener {
            //TODO 发布动态，修改数据库
            val desc = content_push_dynamic?.text?.toString()?.trim()
            val skillContent = content_skill?.text?.toString()?.trim()
            val styleContent = content_style?.text?.toString()?.trim()
            val allItems = mAdapter?.getAllData()
            val itemCount = allItems?.size ?: 0
            val isVideo = allItems?.find { it.type == MediaType.TYPE_VIDEO } != null
            val canSaveData = if (isVideo) {
                itemCount >= 1 //视频目前最多只能有一个
            } else {
                itemCount > 1  //图片需要去除 ADD_TAG 标签
            }
            if (StringUtils.isEmptyOrNull(desc) && StringUtils.isEmptyOrNull(skillContent)
                && StringUtils.isEmptyOrNull(styleContent) && !canSaveData
            ) {
                ToastUtils.showSafeToast(this@PushDynamicActivity, "请选择感兴趣的内容再提交 ^v^")
                return@setOnClickListener
            }
            saveDynamicData(desc, skillContent, styleContent)

        }
        layout_associated_skill?.setOnClickListener {
            //TODO 演艺技能选项
            val skillText = content_skill?.text?.toString()?.trim()
            val intent = Intent()
            intent.setClass(this@PushDynamicActivity, SkillStyleActivity::class.java)
            intent.putExtra(TYPE_ACTING, TYPE_SKILL)
            if (StringUtils.isNotEmptyAndNull(skillText) && (selectedSkillList?.size ?: 0) > 0) {
                intent.putStringArrayListExtra(SkillStyleActivity.BUNDLE_TAGS, selectedSkillList)
            }
            startActivityForResult(intent, REQUEST_SKILL_CODE)
        }
        layout_associated_style?.setOnClickListener {
            //TODO 形象风格选项
            val styleText = content_style?.text?.toString()?.trim()
            val intent = Intent()
            intent.setClass(this@PushDynamicActivity, SkillStyleActivity::class.java)
            intent.putExtra(TYPE_ACTING, TYPE_STYLE)
            if (StringUtils.isNotEmptyAndNull(styleText) && (selectedStyleList?.size ?: 0) > 0) {
                intent.putExtra(SkillStyleActivity.BUNDLE_TAGS, selectedStyleList)
            }
            startActivityForResult(intent, REQUEST_STYLE_CODE)
        }
        layout_associated_location?.setOnClickListener {
            //TODO 地理位置选项
        }

        mAdapter?.setOnPushDynamicImgClickListener(object : PushDynamicImgAdapter.OnPushDynamicImgClickListener {
            override fun onAddPicClick() {
                //进入图片选择和拍照的界面
                val config = ISListConfig.Builder()
                    .multiSelect(true)
                    .rememberSelected(true)
                    .build()
                val canSelVideo = mAdapter?.getAllData()?.find { it.type == MediaType.TYPE_VIDEO } == null
                ISListActivity.startForResult(this@PushDynamicActivity, config, REQUEST_LIST_CODE, canSelVideo)
            }

            override fun onSelItemClick(data: DynamicAdapterBean) {
                val type = data.type
                if (type == MediaType.TYPE_VIDEO) {
                    currentVideo?.let {
                        SimplePlayerActivity.start(this@PushDynamicActivity, it, false)
                    }
                } else {
                    val allDatas = mAdapter?.getAllData() ?: return
                    val filterResult = allDatas.filter { it.type == MediaType.TYPE_IMAGE && it.path != TAG_ADD }
                    val currentPos = filterResult.indexOf(data)
                    if (currentPos < 0) {
                        return
                    }
                    val tempImgs = arrayListOf<String>()
                    filterResult.forEach {
                        tempImgs.add("file://${it.path}")
                    }
                    val intent = Intent(this@PushDynamicActivity, ImageViewPagerActivity::class.java)
                    ImageViewPagerActivity.currentPos = currentPos
                    ImageViewPagerActivity.urls = tempImgs.toMutableList()
                    startActivity(intent)
                }
            }

            override fun onDeleteItem(position: Int) {
                val allDatas = mAdapter?.getAllData()
                val dataSize = allDatas?.size ?: return
                val item = allDatas[position]
                val isVideo = item.type == MediaType.TYPE_VIDEO
                if (position in 0 until dataSize) {
                    mAdapter?.remove(position)
                }
                if (dataSize == 1 && isVideo) {
                    mAdapter?.add(DynamicAdapterBean(TAG_ADD, MediaType.TYPE_IMAGE))
                }
            }

        })

        content_push_dynamic?.addTextChangedListener(mTextWatcher)
    }

    /**
     * 保存数据到数据库
     */
    private fun saveDynamicData(desc: String?, skillContent: String?, styleContent: String?) {

        val allDatas = mAdapter?.getAllData() ?: return
        if (allDatas.size == 0) return
        val videoItem = allDatas.find { it.type == MediaType.TYPE_VIDEO }
        //TODO 视频处理
        val isSaveVideoData = videoItem != null

        NormalDialogUtils.showTextDialog(this, "正在上传中...")
        val newPaths: ArrayList<String> = arrayListOf()
        allDatas.forEach {
            val pathStr = it.path
            if (pathStr != TAG_ADD) {
                newPaths.add(pathStr)
            }
        }
        var videoThumbnailPath = ""
        if (isSaveVideoData) {
            FileUtils.getVideoThumbnailPath(this@PushDynamicActivity, newPaths[0]) {
                videoThumbnailPath = it
            }
            if (videoThumbnailPath.isEmpty()) {
                NormalDialogUtils.dismissNormalDialog()
                ToastUtils.showSafeToast(this@PushDynamicActivity, Constants.ERROR_MSG)
                return
            }
            val videoThumPaths = arrayListOf<String>()
            videoThumPaths.add(videoThumbnailPath)
            //上传缩略图
            Increase.uploadAVFile(videoThumPaths) { isComplete, fileUrls, exception ->
                if (isComplete && exception == null && fileUrls.size > 0) {
                    val thumbnailPath = fileUrls[0]  //只支持一个视频
                    executeUploadFile(newPaths, desc, skillContent, styleContent, thumbnailPath, isSaveVideoData)
                } else {
                    NormalDialogUtils.dismissNormalDialog()
                    val errorMsg = exception?.message ?: Constants.ERROR_MSG
                    ToastUtils.showSafeToast(this@PushDynamicActivity, errorMsg)
                }
            }
        } else {
            executeUploadFile(newPaths, desc, skillContent, styleContent, null, isSaveVideoData)
        }


    }

    /**
     * 上传操作
     */
    private fun executeUploadFile(
        newPaths: ArrayList<String>,
        desc: String?,
        skillContent: String?,
        styleContent: String?,
        videoThumbnail: String?,
        isSaveVideoData: Boolean
    ) {
        Increase.uploadAVFile(newPaths) { isComplete, fileUrls, exception ->
            if (isComplete && exception == null) {
                Increase.uploadDynamicData(fileUrls, desc, skillContent, styleContent, isSaveVideoData,videoThumbnail) {
                    NormalDialogUtils.dismissNormalDialog()
                    if (it == null) {
                        GlobalObserverHelper.uploadDynamicSuccess()
                        TipDialog.show(
                            this, "成功发布,等待后台审核中..."
                            , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH
                        ).setOnDismissListener(object : OnDismissListener {
                            override fun onDismiss() {
                                this@PushDynamicActivity.finish()
                            }
                        })
                    } else {
                        TipDialog.show(this, Constants.ERROR_MSG, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                    }
                }
            } else {
                val errorMsg = exception?.message ?: Constants.ERROR_MSG
                TipDialog.show(this, errorMsg, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
            }
        }
    }

    private val mTextWatcher = object : TextWatcherHelper {
        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val realText = s.toString()
            val length = realText.length
            count_push_dynamic?.text = "$length/140"
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_LIST_CODE -> {
                    val pathList = data.getStringArrayListExtra("result")
                    val pathListSize = pathList?.size ?: return
                    val allSize = PublishConstant.imageMaxSize + PublishConstant.videoMaxSize
                    if (pathListSize < allSize) {
                        pathList.add(TAG_ADD)
                    }
                    val beanList = arrayListOf<DynamicAdapterBean>()
                    pathList.forEach {
                        beanList.add(DynamicAdapterBean(it, MediaType.TYPE_IMAGE))
                    }
                    mAdapter?.clear()
                    mAdapter?.addAll(beanList)

                }
                REQUEST_SKILL_CODE -> {
                    selectedSkillList = data.getStringArrayListExtra(SkillStyleActivity.BUNDLE_TAGS)
                    val builder = StringBuilder()
                    selectedSkillList?.forEach {
                        builder.append("$it ")
                    }
                    content_skill?.text = builder.toString()
                }
                REQUEST_STYLE_CODE -> {
                    selectedStyleList = data.getStringArrayListExtra(SkillStyleActivity.BUNDLE_TAGS)
                    val builder = StringBuilder()
                    selectedStyleList?.forEach {
                        builder.append("$it ")
                    }
                    content_style?.text = builder.toString()
                }

            }
        }
    }

    override fun onDestroy() {
        GlobalObserverHelper.removeObserver(mObservable)
        super.onDestroy()
    }
}