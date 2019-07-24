package com.suncity.dailynotices.ui.activity

import android.content.Intent
import com.avos.avoscloud.*
import com.suncity.dailynotices.R
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.lcoperation.SMSOption
import com.suncity.dailynotices.manager.UserManager
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.ToastUtils
import com.suncity.dailynotices.utils.UIUtils
import kotlinx.android.synthetic.main.ac_setting_verify_code.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SettingVerifyCodeActivity
 * @Description:    输入校验验证码界面
 * @UpdateDate:     10/7/2019
 */
class SettingVerifyCodeActivity : BaseActivity() {

    companion object {
        const val PHONENUM = "phonenum"
        const val NEWPWD = "newpwd"
        const val TYPE_UPDATE = "type_update"
        const val TYPE_UPDATE_PWD = "updatePwd"
        const val TYPE_UPDATE_PHONE = "updatePhone"
        val updateError = Config.getString(R.string.str_update_error)
    }

    private var phoneNum: String = ""
    //更新类型
    private var type: String = ""
    private var newPwd: String = ""

    private var objectId = PreferenceStorage.userObjectId

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_setting_verify_code
    }

    override fun initData() {
        phoneNum = intent.getStringExtra(PHONENUM)
        type = intent.getStringExtra(TYPE_UPDATE)
        newPwd = intent.getStringExtra(NEWPWD)
        tv_title_center?.text = Config.getString(R.string.str_setting)
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        //TODO 需要测试
        tv_edit_confirm?.setOnClickListener {
            val verifyCode = et_value_verifyCode?.text.toString().trim()
            if (verifyCode.isNotEmpty() && UIUtils.isPhoneNumber(this@SettingVerifyCodeActivity, phoneNum)) {
                if (type == TYPE_UPDATE_PHONE) {
                    //更新手机号码
                    updatePhoneNum(verifyCode)
                } else if (type == TYPE_UPDATE_PWD) {
                    //更新密码
                    updatePwd(verifyCode)
                }

            }
        }
    }

    private fun updatePhoneNum(verifyCode: String) {
        SMSOption.verifySmsCode(phoneNum, verifyCode) {
            if (it == null) {
                val userUpdate = AVObject.createWithoutData(TableConstants.TABLE_USER, objectId)
                userUpdate.put(TableConstants.USER_MOBILEPHONENUMBER, phoneNum)
                userUpdate.saveInBackground(object : SaveCallback() {
                    override fun done(e: AVException?) {
                        if (e == null) {
                            ToastUtils.showSafeToast(this@SettingVerifyCodeActivity, "修改成功，请重新登录")
//                            UserManager.removeUserInfo()
//                            startActivity(LoginActivity::class.java)
                            PreferenceStorage.userPhoneNum = phoneNum
                            returnHomeActivity(HomeActivity.POS_MINE)
                        }else{
                            ToastUtils.showSafeToast(this@SettingVerifyCodeActivity, e.message ?: updateError)
                        }
                    }
                })
            } else {
                ToastUtils.showSafeToast(this@SettingVerifyCodeActivity, it.message ?: "验证失败，请重新输入")
            }
        }
    }


    private fun updatePwd(verifyCode: String) {
        if(newPwd.isEmpty() || newPwd.length < 6){
            ToastUtils.showToast("请输入原密码,且密码大于等于 6 位数")
            return
        }
        AVUser.resetPasswordBySmsCodeInBackground(verifyCode,newPwd,object : UpdatePasswordCallback(){

            override fun done(e: AVException?) {
                if (e == null) {
                    ToastUtils.showSafeToast(this@SettingVerifyCodeActivity, "修改成功，请重新登录")
//                    UserManager.removeUserInfo()
//                    startActivity(LoginActivity::class.java)
                    returnHomeActivity(HomeActivity.POS_MINE)
                }else{
                    ToastUtils.showSafeToast(this@SettingVerifyCodeActivity, e.message ?: updateError)
                }
            }

        })
    }

    private fun returnHomeActivity(pos: Int) {
        val intent = Intent()
        intent.setClass(this, HomeActivity::class.java)
        intent.putExtra(HomeActivity.EXTRA_POS, pos)
        startActivity(intent)
        finish()
    }
}