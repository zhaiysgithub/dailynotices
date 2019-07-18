package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.LogUtils
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

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true,0f)
            .init()
    }

    companion object {
        private val REQUEST_LIST_CODE = 0
        private val REQUEST_CAMERA_CODE = 1
    }
    override fun getActivityLayoutId(): Int {
        return R.layout.ac_push_dynamic
    }

    override fun initData() {

    }


    override fun initListener() {

        fl_title_back?.setOnClickListener {
            //TODO 弹出dialog 确认是否需要保存记录
            finish()
        }

        title_right_push_dynamic?.setOnClickListener {
            //TODO 发布动态，修改数据库
        }
        layout_associated_skill?.setOnClickListener {
            //TODO 演艺技能选项
        }
        layout_associated_style?.setOnClickListener {
            //TODO 形象风格选项
        }
        layout_associated_location?.setOnClickListener {
            //TODO 地理位置选项
        }
        layout_camera_add?.setOnClickListener {
            //进入图片选择和拍照的界面
            val config = ISListConfig.Builder()
                .multiSelect(true)
                .rememberSelected(true)
                .build()

            ISNav.getInstance().toListActivity(this,config,REQUEST_LIST_CODE)
        }

        content_push_dynamic?.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object: TextWatcherHelper{
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

            // draweeView.setImageURI(Uri.parse("file://"+pathList.get(0)));
            val paths = StringBuilder()
            for (path in pathList) {
                paths.append(path + "\n")
            }
            LogUtils.e("PushDynamicActivity -> paths=$paths")
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val path = data.getStringExtra("result")
            LogUtils.e("PushDynamicActivity -> paths=${path + "\n"}")
        }
    }
}