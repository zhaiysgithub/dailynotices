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

    /**
     * 实名认证成功
     */
    fun onUpdateAutonymSuccess()

    /**
     * 用户更新图片的监听
     * @picLocalPaths 本地图片的集合
     */
    fun onUserPicUpdateListener(picLocalPaths: ArrayList<String>)

    /**
     * 更新我查看的数据
     */
    fun onNotifyRecentVisitUser()

    /**
     * 刪除帖子通知帖子數據
     */
    fun notifyDelPost(postId: String)
}