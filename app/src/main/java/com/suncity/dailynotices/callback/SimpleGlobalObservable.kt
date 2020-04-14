package com.suncity.dailynotices.callback

import com.suncity.dailynotices.islib.bean.LocalMedia

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      SimpleGlobalObservable
 * @Description:     作用描述
 */

open class SimpleGlobalObservable : GlobalObservable {

    override fun onUpdateUserinfoSuccess() {
    }

    override fun onLoginSuccess() {
    }

    override fun onLogoutSuccess() {
    }

    override fun onUploadDynamicSuccess() {
    }

    override fun onUpdateAutonymSuccess() {
    }

    override fun onUserPicUpdateListener(picLocalPaths: ArrayList<String>) {

    }

    override fun onNotifyRecentVisitUser() {
    }

    override fun notifyDelPost(postId: String) {
    }

    override fun onVideoSelected(videoMedia: LocalMedia) {
    }


}