package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.widget.Toast
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import kotlinx.android.synthetic.main.ac_push_dynamic.*

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
}