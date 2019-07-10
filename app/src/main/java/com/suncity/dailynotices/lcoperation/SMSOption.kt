package com.suncity.dailynotices.lcoperation

import com.avos.avoscloud.*
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.Config

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.lcoperation
 * @ClassName:      SMSOption
 * @Description:     短信验证码的操作
 * @UpdateDate:     10/7/2019
 */
object SMSOption {

    //调用接口发送验证短信
    fun requestSms(phoneNum: String,callback:((AVException?) -> Unit)) {
        val appName = Config.getString(R.string.app_name)
        val smsOption = AVSMSOption()
        smsOption.setTtl(10) // 验证码有效时间为 10 分钟
        smsOption.setApplicationName(appName)
        smsOption.setOperation("修改登录手机号")
        AVSMS.requestSMSCodeInBackground(phoneNum,smsOption,object : RequestMobileCodeCallback(){
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }

    //调用接口验证用户输入的验证码是否有效
    fun verifySmsCode(phoneNum: String,verifyCode:String,callback:((AVException?) -> Unit)){

        AVSMS.verifySMSCodeInBackground(verifyCode,phoneNum,object : AVMobilePhoneVerifyCallback(){
            override fun done(e: AVException?) {
                callback(e)
            }

        })
    }
}