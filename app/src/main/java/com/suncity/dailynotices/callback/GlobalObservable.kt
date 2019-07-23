package com.suncity.dailynotices.callback

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      GlobalObservable
 * @Description:    全局的广播通知类
 * @UpdateDate:     31/5/2019
 */
interface GlobalObservable {


    /**
     * 登录成功
     */
    fun onLoginSuccess()

    /**
     * 登出成功
     */
    fun onLogoutSuccess()

    /**
     * 更新用户数据成功
     */
    fun onUpdateUserinfoSuccess()

    /**
     * 发布动态成功
     */
    fun onUploadDynamicSuccess()
}