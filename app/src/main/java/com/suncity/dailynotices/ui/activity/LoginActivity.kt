package com.suncity.dailynotices.ui.activity

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.IEditTextChangeListener
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.dialog.OnDismissListener
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.HomeActivity.Companion.POS_MINE
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_login.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      LoginActivity
 * @Description:    用户没有登录进入此页面执行登录的逻辑
 * 参考文档 https://leancloud.cn/docs/leanstorage-started-android.html#hash-814093878
 */
class LoginActivity : BaseActivity() {

    private val loadingText = Config.getString(R.string.str_loading)
    private val loginSuccessText = Config.getString(R.string.str_login_success)
    private val loginErrorText = Config.getString(R.string.str_login_error)

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .titleBar(R.id.fl_login_del, false)
            .statusBarDarkFont(true, 0f)
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
        spannableStr.setSpan(AbsoluteSizeSpan(dealSize), 12, aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStr.setSpan(ForegroundColorSpan(dealColor), 12, aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_login_agreement_tip?.text = spannableStr

        val etPhoneNum = findViewById<EditText>(R.id.et_phoneNumber)
        val etPwd = findViewById<EditText>(R.id.et_pwd)
        val submitText = findViewById<TextView>(R.id.tv_login)
        val textChangeListener = TextChangeListenerUtils(submitText)
        textChangeListener.addAllEditText(arrayOf(etPhoneNum, etPwd))
        textChangeListener.setChangeListener(iEditTextChangeListener)
    }

    override fun initListener() {
        fl_login_del?.setOnClickListener {
            finish()
        }
        tv_login_agreement_tip?.setOnClickListener {
            startActivity(UserAgreementActivity::class.java, false)

        }
        tv_forget_pwd?.setOnClickListener {
            startActivity(EditPwdActivity::class.java)
        }

        tv_login?.setOnClickListener {
            startLogin()
        }

        et_phoneNumber?.addTextChangedListener(object : TextWatcherHelper{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                if (length > 0){
                    iv_del_phone?.visibility = View.VISIBLE
                }else{
                    iv_del_phone?.visibility = View.GONE
                }
            }
        })
        et_pwd?.addTextChangedListener(object : TextWatcherHelper{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                if (length > 0){
                    iv_del_pwd?.visibility = View.VISIBLE
                }else{
                    iv_del_pwd?.visibility = View.GONE
                }
            }
        })

        iv_del_phone?.setOnClickListener {
            et_phoneNumber?.setText("")
        }
        iv_del_pwd?.setOnClickListener {
            et_pwd?.setText("")
        }
    }

    private fun startLogin() {
        val phoneNumber = et_phoneNumber?.text.toString().trim()
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast("请输入手机号码")
            return
        }
        if (!UIUtils.isPhoneNumber(this, phoneNumber)) {
            ToastUtils.showToast("手机号码有误，请重新输入...")
            return
        }
        val pwd = et_pwd.text.toString().trim()
        if (UIUtils.isPhoneNumber(this, phoneNumber)) {
            if (TextUtils.isEmpty(pwd) || pwd.length < 6) {
                ToastUtils.showToast("请重新输入密码")
            } else {
                ProgressUtil.showProgress(this)
                requestLoginWithLeancloud(phoneNumber, pwd)
            }
        }
    }

    /**
     * 使用leanCloud登录
     */
    private fun requestLoginWithLeancloud(phoneNumber: String, pwd: String) {
        AVUser.loginByMobilePhoneNumberInBackground(phoneNumber, pwd, object : LogInCallback<AVUser>() {
            override fun done(user: AVUser?, e: AVException?) {
                ProgressUtil.hideProgress()
                if (user != null && e == null) {
                    this@LoginActivity.finish()
                    val tipDialog = TipDialog.show(this@LoginActivity,loginSuccessText
                        ,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_ERROR)
                    tipDialog.setOnDismissListener(object : OnDismissListener{
                        override fun onDismiss() {
                            HomeActivity.start(this@LoginActivity, POS_MINE)
                        }
                    })

                } else {
                    if(e != null){
                        TipDialog.show(this@LoginActivity,e.message ?: loginErrorText
                            ,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_ERROR)
                    }else{
                        TipDialog.show(this@LoginActivity,loginErrorText
                            ,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_ERROR)
                    }
                }
            }

        })
    }




    private val iEditTextChangeListener = object : IEditTextChangeListener {
        override fun onTextChange(hasContent: Boolean) {
            if (hasContent) {
                tv_login?.setTextColor(Config.getColor(R.color.color_222))
                tv_login?.background = Config.getDrawable(R.drawable.shape_submit_checked_bg)
            } else {
                tv_login?.setTextColor(Config.getColor(R.color.color_999))
                tv_login?.background = Config.getDrawable(R.drawable.shape_submit_unchecked_bg)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}