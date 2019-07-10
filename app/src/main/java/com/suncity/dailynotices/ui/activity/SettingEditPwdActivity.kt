package com.suncity.dailynotices.ui.activity

import android.content.Intent
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.RequestMobileCodeCallback
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.SettingVerifyCodeActivity.Companion.TYPE_UPDATE_PWD
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_setting_edit_detail.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SettingEditPwdActivity
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingEditPwdActivity : BaseActivity() {

    companion object {
        private val STR_ORIGIN_PWD = Config.getString(R.string.str_origin_pwd_input)
        private val STR_NEW_PWD = Config.getString(R.string.str_new_pwd_input)
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
        tv_desc_origin?.text = STR_ORIGIN_PWD
        tv_desc_new?.text = STR_NEW_PWD
    }


    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

        tv_edit_confirm?.setOnClickListener {
            val originPwd = et_value_origin?.text.toString().trim()
            if(originPwd.isEmpty() || originPwd.length < 6){
                ToastUtils.showToast("请输入原密码,且密码大于等于 6 位数")
                return@setOnClickListener
            }
            val newPwd = et_value_new?.text.toString().trim()
            if(newPwd.isEmpty() || newPwd.length < 6){
                ToastUtils.showToast("请输入原密码,且密码大于等于 6 位数")
                return@setOnClickListener
            }

            val phoneNum = PreferenceStorage.userPhoneNum
            //使用手机号码重置密码
            AVUser.requestPasswordResetBySmsCodeInBackground(phoneNum,object : RequestMobileCodeCallback(){

                override fun done(e: AVException?) {
                    if(e == null){
                        val intent  = Intent()
                        intent.setClass(this@SettingEditPwdActivity,SettingVerifyCodeActivity::class.java)
                        intent.putExtra(SettingVerifyCodeActivity.PHONENUM,phoneNum)
                        intent.putExtra(SettingVerifyCodeActivity.NEWPWD,newPwd)
                        intent.putExtra(SettingVerifyCodeActivity.TYPE_UPDATE,TYPE_UPDATE_PWD)
                        startActivity(intent)
                    }else{
                        //TODO
                        ToastUtils.showSafeToast(this@SettingEditPwdActivity,"获取验证码失败，请稍后再试")
                    }

                }

            })


        }
    }
}