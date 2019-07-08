package com.suncity.dailynotices.ui.activity

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.DisplayUtils
import kotlinx.android.synthetic.main.ac_login.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      LoginActivity
 * @Description:    用户没有登录进入此页面执行登录的逻辑
 */
class LoginActivity : BaseActivity() {

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .titleBar(R.id.fl_login_del, false)
            .statusBarDarkFont(true,0f)
            .transparentBar()
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_login
    }

    override fun initData() {
        //设置使用协议的字体大小和颜色
        val dealSize = DisplayUtils.sp2px(14f).toInt()
        val dealColor = Config.getColor(R.color.color_black)
        val aggrementStr = Config.getString(R.string.str_login_agreement_tip)
        val spannableStr = SpannableString(aggrementStr)
        spannableStr.setSpan(AbsoluteSizeSpan(dealSize),12,aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        spannableStr.setSpan(ForegroundColorSpan(dealColor),12,aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        tv_login_agreement_tip?.text = spannableStr
    }

    override fun initListener() {
        fl_login_del?.setOnClickListener {
            finish()
        }
        tv_login_agreement_tip?.setOnClickListener {
            startActivity(UserAgreementActivity::class.java,false)

        }
        tv_forget_pwd?.setOnClickListener {
            startActivity(EditPwdActivity::class.java)
        }
    }
}