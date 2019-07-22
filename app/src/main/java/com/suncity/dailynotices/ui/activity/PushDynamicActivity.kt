package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.PushDynamicImgAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.DisplayUtils
import com.suncity.dailynotices.utils.LogUtils
import kotlinx.android.synthetic.main.ac_push_dynamic.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      PushDynamicActivity
 * @Description:    发布动态的页面
 * @UpdateDate:     18/7/2019
 */
class PushDynamicActivity : BaseActivity() {

    private var mAdapter: PushDynamicImgAdapter? = null


    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    companion object {
        private val REQUEST_LIST_CODE = 0
        private val REQUEST_CAMERA_CODE = 1
        const val TAG_ADD = "addTag"
        val REQUEST_SKILL_CODE = 2
        val REQUEST_STYLE_CODE = 3
        val TYPE_SKILL = "type_skill"
        val TYPE_STYLE = "type_style"
        val TYPE_ACTING = "type_acting"
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_push_dynamic
    }

    override fun initData() {
        mAdapter = PushDynamicImgAdapter(this)
        recyclerView_selected_pic?.setHasFixedSize(true)
        recyclerView_selected_pic?.layoutManager = GridLayoutManager(this, 3)
        recyclerView_selected_pic?.addItemDecoration(itemDecoration)
        recyclerView_selected_pic?.adapter = mAdapter
        mAdapter?.add(TAG_ADD)
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

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            //TODO 弹出dialog 确认是否需要保存记录
            finish()
        }

        title_right_push_dynamic?.setOnClickListener {
            //TODO 发布动态，修改数据库
            val desc = content_push_dynamic?.text?.toString()?.trim()

        }
        layout_associated_skill?.setOnClickListener {
            //TODO 演艺技能选项
            val intent = Intent()
            intent.setClass(this@PushDynamicActivity, SkillStyleActivity::class.java)
            intent.putExtra(TYPE_ACTING, TYPE_SKILL)
            startActivityForResult(intent, REQUEST_SKILL_CODE)
        }
        layout_associated_style?.setOnClickListener {
            //TODO 形象风格选项
            val intent = Intent()
            intent.setClass(this@PushDynamicActivity, SkillStyleActivity::class.java)
            intent.putExtra(TYPE_ACTING, TYPE_STYLE)
            startActivityForResult(intent, REQUEST_STYLE_CODE)
        }
        layout_associated_location?.setOnClickListener {
            //TODO 地理位置选项
        }

        mAdapter?.SetOnPushDynamicImgClickListener(object : PushDynamicImgAdapter.OnPushDynamicImgClickListener {
            override fun onAddPicClick() {
                //进入图片选择和拍照的界面
                val config = ISListConfig.Builder()
                    .multiSelect(true)
                    .rememberSelected(true)
                    .build()

                ISListActivity.startForResult(this@PushDynamicActivity, config, REQUEST_LIST_CODE)
            }

            override fun onSelItemClick(url: String) {
            }

        })

        content_push_dynamic?.addTextChangedListener(mTextWatcher)
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
        if (requestCode == REQUEST_LIST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val pathList = data.getStringArrayListExtra("result")
            pathList.add(TAG_ADD)

            if (pathList != null && pathList.size > 0) {
                mAdapter?.clear()
                mAdapter?.addAll(pathList)
            }
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val path = data.getStringExtra("result")
            LogUtils.e("1----PushDynamicActivity -> paths=${path + "\n"}")
        } else if (requestCode == REQUEST_SKILL_CODE && resultCode == Activity.RESULT_OK && data != null) {

        } else if (requestCode == REQUEST_STYLE_CODE && resultCode == Activity.RESULT_OK && data != null) {

        }
    }
}