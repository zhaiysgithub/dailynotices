package com.suncity.dailynotices.ui.activity

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.RequestMobileCodeCallback
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.IEditTextChangeListener
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_edit_pwd.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      EditPwdActivity
 * @Description:    修改密码页面
 */
class EditPwdActivity : BaseActivity() {

    companion object {
        private val phoneNumEmpty = Config.getString(R.string.str_phonenum_empty)
        private val phoneNumError = Config.getString(R.string.str_phonenum_error)
        private val smscodeSuccess = Config.getString(R.string.str_send_smscode_success)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .titleBar(R.id.fl_editpwd_del, false)
            .statusBarDarkFont(true, 0f)
            .transparentBar()
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_edit_pwd
    }

    override fun initData() {

        val etPhoneNum = findViewById<EditText>(R.id.et_phoneNumber)
        val etAuthCode = findViewById<EditText>(R.id.et_authcode)
        val etNewPwd = findViewById<EditText>(R.id.et_new_pwd)
        val submitText = findViewById<TextView>(R.id.tv_submit)

        val etTextChangeListener = TextChangeListenerUtils(submitText)
        etTextChangeListener.addAllEditText(arrayOf(etPhoneNum, etAuthCode, etNewPwd))
        etTextChangeListener.setChangeListener(iChangeListener)

    }

    override fun initListener() {
        fl_editpwd_del?.setOnClickListener {
            finish()
        }
        tv_get_authcode?.setOnClickListener {
            getAuthCode()
        }

        et_phoneNumber?.addTextChangedListener(object : TextWatcherHelper {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeDelIconVisible(s,iv_del_phonenum)
            }
        })
        et_authcode?.addTextChangedListener(object : TextWatcherHelper{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeDelIconVisible(s,iv_del_authcode)
            }
        })
        et_new_pwd?.addTextChangedListener(object : TextWatcherHelper{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeDelIconVisible(s,iv_del_newpwd)
            }
        })
    }

    private fun changeDelIconVisible(s: CharSequence?, delView: ImageView?) {
        val length = s?.length ?: 0
        if (length > 0) {
            delView?.visibility = View.VISIBLE
        } else {
            delView?.visibility = View.GONE
        }
    }

    private fun getAuthCode() {
        val phoneNum = et_phoneNumber?.text.toString().trim()
        if (StringUtils.isEmpty(phoneNum)) {
            ToastUtils.showToast(phoneNumEmpty)
            return
        }
        if (!UIUtils.isPhoneNumber(this, phoneNum)) {
            ToastUtils.showToast(phoneNumError)
            return
        }
        //发送验证码
        AVUser.requestPasswordResetBySmsCodeInBackground(phoneNum, object : RequestMobileCodeCallback() {
            override fun done(e: AVException?) {
                if (filterException(e)) {
                    ToastUtils.showSafeToast(this@EditPwdActivity, smscodeSuccess)
                }
            }
        })

    }

    private val iChangeListener = object : IEditTextChangeListener {

        override fun onTextChange(hasContent: Boolean) {
            if (hasContent) {
                tv_submit?.setTextColor(Config.getColor(R.color.color_222))
                tv_submit?.background = Config.getDrawable(R.drawable.shape_submit_checked_bg)
            } else {
                tv_submit?.setTextColor(Config.getColor(R.color.color_999))
                tv_submit?.background = Config.getDrawable(R.drawable.shape_submit_unchecked_bg)
            }
        }

    }


}