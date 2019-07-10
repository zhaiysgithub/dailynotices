package com.suncity.dailynotices.ui.activity

import android.content.Intent
import com.suncity.dailynotices.R
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.lcoperation.SMSOption
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.SettingVerifyCodeActivity.Companion.TYPE_UPDATE_PHONE
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.ToastUtils
import com.suncity.dailynotices.utils.UIUtils
import kotlinx.android.synthetic.main.ac_setting_edit_detail.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SettingEditPhoneActivity
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingEditPhoneActivity : BaseActivity() {

    companion object {
        private val STR_ORIGIN_PHONE = Config.getString(R.string.str_origin_phone_input)
        private val STR_NEW_PHONE = Config.getString(R.string.str_new_phone_input)
        private val STR_VERIFYCODE_TOAST = Config.getString(R.string.str_verifycode_toast)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .init()
    }
    override fun getActivityLayoutId(): Int {
        return R.layout.ac_setting_edit_detail
    }

    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_setting)
        tv_desc_origin?.text = STR_ORIGIN_PHONE
        tv_desc_new?.text = STR_NEW_PHONE
        val encryPhone = UIUtils.getEncryPhone(PreferenceStorage.userPhoneNum)
        if (encryPhone.isNotEmpty()){
            et_value_origin?.setText(encryPhone)
        }
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

        tv_edit_confirm?.setOnClickListener {
            val originPhoneNumber = et_value_origin?.text.toString().trim()
            if(originPhoneNumber.isEmpty() || !UIUtils.isPhoneNumber(this, originPhoneNumber)){
                ToastUtils.showToast("请输入原手机号码")
                return@setOnClickListener
            }
            val newPhoneNumber = et_value_new?.text.toString().trim()
            if(newPhoneNumber.isEmpty() || !UIUtils.isPhoneNumber(this, newPhoneNumber)){
                ToastUtils.showToast("请输入原手机号码")
                return@setOnClickListener
            }

            SMSOption.requestSms(newPhoneNumber){
                if(it == null){
                    //验证码发送成功
                    TipDialog.show(
                        this@SettingEditPhoneActivity, STR_VERIFYCODE_TOAST
                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH
                    )

                    val intent  = Intent()
                    intent.setClass(this@SettingEditPhoneActivity,SettingVerifyCodeActivity::class.java)
                    intent.putExtra(SettingVerifyCodeActivity.PHONENUM,newPhoneNumber)
                    intent.putExtra(SettingVerifyCodeActivity.TYPE_UPDATE,TYPE_UPDATE_PHONE)
                    startActivity(intent)
                }else{
                    //TODO
                    ToastUtils.showSafeToast(this@SettingEditPhoneActivity,"获取验证码失败，请稍后再试")
                }
            }


        }
    }
}